output "api_base_url" {
  value = "${aws_apigatewayv2_api.upload_api.api_endpoint}/"
}
