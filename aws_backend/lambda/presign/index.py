import boto3  # type: ignore
import os
import json
import uuid
from datetime import datetime

s3 = boto3.client("s3")
dynamodb = boto3.resource("dynamodb")
BUCKET = os.environ["UPLOAD_BUCKET"]
TABLE = os.environ["TASK_TABLE"]


def lambda_handler(event, context):
    task_id = str(uuid.uuid4())
    key = f"uploads/{task_id}"

    presigned_url = s3.generate_presigned_url(
        "put_object",
        Params={"Bucket": BUCKET, "Key": key, "ContentType": "application/zip"},
        ExpiresIn=300,  # 5 minutes
    )

    table = dynamodb.Table(TABLE)
    table.put_item(
        Item={
            "task_id": task_id,
            "status": "waiting",
            "created_at": datetime.utcnow().isoformat(),
        }
    )

    return {
        "statusCode": 200,
        "headers": {"Content-Type": "application/json"},
        "body": json.dumps({"task_id": task_id, "upload_url": presigned_url}),
    }
