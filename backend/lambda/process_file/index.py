import boto3  # type: ignore
import os
import json
import io
import zipfile
import base64
import re
from urllib.parse import unquote_plus
from google.cloud import vision

# AWS clients
s3 = boto3.client("s3")
dynamodb = boto3.resource("dynamodb")
bedrock = boto3.client("bedrock-runtime")

# ENV variables
BUCKET = os.environ["UPLOAD_BUCKET"]
TABLE = os.environ["TASK_TABLE"]
GCP_CREDS_B64 = os.environ["GCP_CREDENTIALS_BASE64"]

# Set Google credentials
creds_path = "/tmp/gcp-key.json"
with open(creds_path, "w") as f:
    f.write(base64.b64decode(GCP_CREDS_B64).decode("utf-8"))
os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = creds_path
vision_client = vision.ImageAnnotatorClient()


def lambda_handler(event, context):
    for record in event.get("Records", []):
        try:
            s3_event = json.loads(record["body"])
            s3_record = s3_event["Records"][0]

            key = unquote_plus(s3_record["s3"]["object"]["key"])
            task_id = key.split("/")[-1]

            s3_object = s3.get_object(Bucket=BUCKET, Key=key)
            zip_bytes = s3_object["Body"].read()

            # Extract text
            extracted_text = ""
            with zipfile.ZipFile(io.BytesIO(zip_bytes)) as zipf:
                for name in zipf.namelist():
                    if name.lower().endswith((".png", ".jpg", ".jpeg", ".bmp")):
                        with zipf.open(name) as image_file:
                            img_bytes = image_file.read()
                            image = vision.Image(content=img_bytes)

                            response = vision_client.document_text_detection(
                                image=image
                            )

                            if response.error.message:
                                raise RuntimeError(
                                    f"Vision OCR error: {response.error.message}"
                                )

                            text = response.full_text_annotation.text
                            extracted_text += text + "\n"

            # Extract entities
            entities = extract_entities_from_text(extracted_text)

            # Update DynamoDB
            table = dynamodb.Table(TABLE)
            table.update_item(
                Key={"task_id": task_id},
                UpdateExpression="SET #s = :status, #t = :text, entities = :entities",
                ExpressionAttributeNames={"#s": "status", "#t": "text"},
                ExpressionAttributeValues={
                    ":status": "done",
                    ":text": extracted_text,
                    ":entities": entities,
                },
            )

        except Exception as e:
            print(f"Error processing record: {e}")
            print(f"Raw record: {record}")
            try:
                task_id = task_id if "task_id" in locals() else "unknown"
                table = dynamodb.Table(TABLE)
                table.update_item(
                    Key={"task_id": task_id},
                    UpdateExpression="SET #s = :status",
                    ExpressionAttributeNames={"#s": "status"},
                    ExpressionAttributeValues={":status": "error"},
                )
            except Exception as update_error:
                print(f"Failed to update task status to error: {update_error}")


def extract_entities_from_text(text: str) -> list:
    prompt = f"""
            You are an expert system for extracting named entities from historical documents written in different languages (Cyrillic or Latin scripts), including ancient languages.

            The documents may include formal and legal or ancient language, personal names, addresses, dates, and government institutions.

            Your task is to extract entities from the text and return a clean valid JSON object exactly matching the following schema:

            {{
                "people": [],
                "organizations": [],
                "locations": [],
                "dates": [],
                etc...
            }}

            IMPORTANT:
            - The JSON object must be valid with correct structure and syntax following the schema above.
            - All field values must be flat **lists of strings**.
            - Each key must use **double quotes**.
            - Do not use nested objects — every value must be a flat list.
            - Do **not** truncate output. Make sure the JSON is fully closed (matching {{ and }} and all brackets).
            - The output must be **pure JSON only** — do not include any explanations, markdown, or commentary.

            Notes:
            - Respond only with a valid JSON object.
            - Do not include explanations or comments.
            - Preserve original spelling in Cyrillic (do not translate).
            - Correct misspellings.
            - Above categories are examples; you can add more or fewer categories if needed.
            - All field values must be flat lists of strings.

            Text:
            {text}
        """

    body = {
        "anthropic_version": "bedrock-2023-05-31",
        "max_tokens": 200,
        "top_k": 250,
        "stop_sequences": [],
        "temperature": 0.2,
        "top_p": 0.95,
        "messages": [{"role": "user", "content": [{"type": "text", "text": prompt}]}],
    }

    response = bedrock.invoke_model(
        modelId="arn:aws:bedrock:us-east-1:859059810646:inference-profile/us.anthropic.claude-3-5-haiku-20241022-v1:0",
        contentType="application/json",
        accept="application/json",
        body=json.dumps(body),
    )

    response_body = json.loads(response["body"].read())
    message_text = response_body["content"][0]["text"].strip()

    try:
        return json.loads(message_text)
    except json.JSONDecodeError:
        print("[WARN] Failed to decode Claude JSON response:")
        print(message_text)

        # Attempt to recover by truncating at the last closing list bracket and appending a closing brace
        last_list_close = message_text.rfind(']')
        if last_list_close != -1:
            truncated = message_text[:last_list_close + 1]
            # Remove trailing comma if it exists
            truncated = re.sub(r',\s*$', '', truncated)
            fixed_json = truncated + '\n}'

            try:
                return json.loads(fixed_json)
            except json.JSONDecodeError:
                print("[ERROR] Failed to recover JSON after truncation.")
                return []
        else:
            print("[ERROR] Could not find a closing list bracket `]` to attempt JSON fix.")
            return []
