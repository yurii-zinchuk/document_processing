resource "aws_s3_bucket" "uploads" {
  bucket = "ocr-upload-bucket-${random_id.suffix.hex}"
  force_destroy = true

  tags = {
    Name = "OCR Uploads"
  }
}

resource "aws_s3_bucket_notification" "s3_to_sqs" {
  bucket = aws_s3_bucket.uploads.id

  queue {
    queue_arn = aws_sqs_queue.ocr_queue.arn
    events    = ["s3:ObjectCreated:*"]
  }

  depends_on = [aws_sqs_queue_policy.allow_s3]
}
