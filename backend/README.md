# Backend

## Description

This directory contains all Infrastructure-as-Code (IaC) definitions for the backend of the Historical Document Processing System. It provisions AWS services using Terraform and manages the cloud-native workflow that enables document upload, OCR, and named entity extraction.

The infrastructure is deployed and managed through Terraform Cloud, allowing consistent provisioning and system independance.

## Structure

The Terraform configuration is split into logical files for clarity:

- **`main.tf`** — Entry point, backend config, provider declaration
- **`s3.tf`** — S3 bucket for document uploads
- **`iam.tf`** — IAM roles and policies for Lambda execution
- **`dynamodb.tf`** — DynamoDB table for task result storage
- **`lambdas.tf`** — Lambda functions and related configurations
- **`apigateway.tf`** — HTTP API Gateway for frontend-backend communication
- **`sqs.tf`** — Queue for processing triggers
- **`ecr.tf`** — Container registry for image-based Lambda deployment

## Components

### S3
- Bucket for uploading ZIP files of scanned images
- S3 events trigger the processing pipeline

### Lambda Functions
- `generate_presigned_url` — Issues upload URLs for the mobile app
- `process_file_lambda` — Performs OCR and NER from uploaded files (deployed as a container image)
- `get_task_lambda` — Allows the mobile app to query processing results

### SQS
- Acts as a decoupling layer between S3 and processing Lambda
- Ensures reliable and asynchronous execution

### DynamoDB
- Stores processing tasks, their status, and extracted entities
- Acts as the main source of truth for task queries

### API Gateway
- Serves endpoints used by the mobile frontend
- Exposes task creation and status retrieval

### ECR
- Hosts the Docker image for the `process_file_lambda`
- Enables packaging of large dependencies like Google Cloud Vision

---

For mobile configuration, see [`mobile/README.md`](../mobile/README.md).