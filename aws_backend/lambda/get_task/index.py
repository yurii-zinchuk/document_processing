import boto3  # type: ignore
import os
import json

dynamodb = boto3.resource("dynamodb")
TABLE = os.environ["TASK_TABLE"]


def lambda_handler(event, context):
    task_id = event["pathParameters"]["task_id"]

    table = dynamodb.Table(TABLE)
    response = table.get_item(Key={"task_id": task_id})

    if "Item" not in response:
        return {"statusCode": 404, "body": json.dumps({"error": "Task not found"})}

    item = response["Item"]

    if item["status"] == "waiting":
        return {
            "statusCode": 200,
            "body": json.dumps({"status": item["status"], "result": {}}),
        }

    return {
        "statusCode": 200,
        "body": json.dumps(
            {
                "status": item["status"],
                "result": {
                    "text": item.get("text", ""),
                    "entities": item.get("entities", []),
                },
            }
        ),
    }
