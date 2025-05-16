resource "aws_lambda_permission" "allow_apigw_invoke" {
  statement_id  = "AllowExecutionFromAPIGateway"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.generate_presigned_url.function_name
  principal     = "apigateway.amazonaws.com"
  source_arn    = "${aws_apigatewayv2_api.upload_api.execution_arn}/*/*"
}

data "archive_file" "presign_lambda_zip" {
  type        = "zip"
  source_dir  = "${path.module}/../lambda/presign"
  output_path = "${path.module}/../lambda/presign.zip"
}

resource "aws_lambda_function" "generate_presigned_url" {
  function_name = "generate_presigned_url"
  role          = aws_iam_role.lambda_exec.arn
  handler       = "index.lambda_handler"
  runtime       = "python3.11"
  filename      = data.archive_file.presign_lambda_zip.output_path
  source_code_hash = data.archive_file.presign_lambda_zip.output_base64sha256

  environment {
    variables = {
      UPLOAD_BUCKET = aws_s3_bucket.uploads.bucket
      TASK_TABLE    = aws_dynamodb_table.tasks.name
    }
  }

  tags = {
    Name = "GeneratePresignedUrlLambda"
  }
}

resource "aws_lambda_function" "process_file" {
  function_name = "process_file_lambda"
  role          = aws_iam_role.lambda_exec.arn
  package_type  = "Image"

  image_uri     = "${aws_ecr_repository.ocr_lambda.repository_url}@sha256:86a566ae5aa6e271a62c352b65a46170c3e78b9cb42908c4913d76efcb312029"

  timeout       = 30 # seconds

  environment {
    variables = {
      UPLOAD_BUCKET           = aws_s3_bucket.uploads.bucket
      TASK_TABLE              = aws_dynamodb_table.tasks.name
      GCP_CREDENTIALS_BASE64  = base64encode(file("${path.module}/../gcp-key.json"))
    }
  }
}

resource "aws_lambda_permission" "allow_apigw_get_task" {
  statement_id  = "AllowExecutionFromAPIGatewayGetTask"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.get_task.function_name
  principal     = "apigateway.amazonaws.com"
  source_arn    = "${aws_apigatewayv2_api.upload_api.execution_arn}/*/*"
}

data "archive_file" "get_task_zip" {
  type        = "zip"
  source_dir  = "${path.module}/../lambda/get_task"
  output_path = "${path.module}/../lambda/get_task.zip"
}

resource "aws_lambda_function" "get_task" {
  function_name = "get_task_lambda"
  role          = aws_iam_role.lambda_exec.arn
  handler       = "index.lambda_handler"
  runtime       = "python3.11"
  filename      = data.archive_file.get_task_zip.output_path
  source_code_hash = data.archive_file.get_task_zip.output_base64sha256

  environment {
    variables = {
      TASK_TABLE = aws_dynamodb_table.tasks.name
    }
  }
}
