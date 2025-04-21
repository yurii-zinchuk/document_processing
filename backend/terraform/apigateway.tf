resource "aws_apigatewayv2_api" "upload_api" {
  name          = "ocr-upload-api"
  protocol_type = "HTTP"
}

resource "aws_apigatewayv2_integration" "upload_lambda_integration" {
  api_id             = aws_apigatewayv2_api.upload_api.id
  integration_type   = "AWS_PROXY"
  integration_uri    = aws_lambda_function.generate_presigned_url.invoke_arn
  integration_method = "POST"
  payload_format_version = "2.0"
}

resource "aws_apigatewayv2_route" "upload_route" {
  api_id    = aws_apigatewayv2_api.upload_api.id
  route_key = "GET /tasks/create"
  target    = "integrations/${aws_apigatewayv2_integration.upload_lambda_integration.id}"
}

resource "aws_apigatewayv2_stage" "default_stage" {
  api_id      = aws_apigatewayv2_api.upload_api.id
  name        = "$default"
  auto_deploy = true
}

resource "aws_apigatewayv2_integration" "get_task_lambda_integration" {
  api_id           = aws_apigatewayv2_api.upload_api.id
  integration_type = "AWS_PROXY"
  integration_uri  = aws_lambda_function.get_task.invoke_arn
  integration_method = "POST"
  payload_format_version = "2.0"
}

resource "aws_apigatewayv2_route" "get_task_route" {
  api_id    = aws_apigatewayv2_api.upload_api.id
  route_key = "GET /tasks/{task_id}/status"
  target    = "integrations/${aws_apigatewayv2_integration.get_task_lambda_integration.id}"
}
