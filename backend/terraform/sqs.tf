resource "aws_sqs_queue" "ocr_queue" {
  name = "ocr-processing-queue"
}

resource "aws_sqs_queue_policy" "allow_s3" {
  queue_url = aws_sqs_queue.ocr_queue.id

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Principal = "*",
        Action = "SQS:SendMessage",
        Resource = aws_sqs_queue.ocr_queue.arn,
        Condition = {
          ArnEquals = {
            "aws:SourceArn" = aws_s3_bucket.uploads.arn
          }
        }
      }
    ]
  })
}

resource "aws_lambda_event_source_mapping" "sqs_to_lambda" {
  event_source_arn = aws_sqs_queue.ocr_queue.arn
  function_name    = aws_lambda_function.process_file.arn
  batch_size       = 1
}
