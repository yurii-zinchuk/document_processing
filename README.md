# Historical Document Processing System

This project is a serverless system for processing scanned historical documents. It performs OCR (Optical Character Recognition) and NER (Named Entity Recognition) using cloud infrastructure and exposes the results to a mobile app.

## Project Structure

- `mobile/` — Android application for uploading scans and displaying results.
- `backend/` — Terraform configuration for deploying AWS infrastructure.

## Technologies Used

### Frontend Overview
A mobile application allows users to upload scanned documents and view extracted results. It handles file zipping, network communication, and presents structured NER output in a user-friendly format.

### Backend Overview
The backend is fully cloud-native and event-driven. It receives uploaded files, processes them asynchronously, performs OCR and entity extraction, and stores results for retrieval by the app.

### Infrastructure as Code
The entire backend infrastructure is managed using Terraform and deployed via Terraform Cloud.

### The setup
The system is currently up, but may not be in the future.

## Explore More

- Read more about the backend in [`backend/`](./backend/README.md)
- Read more about the Android app in [`mobile/`](./mobile/README.md)

---

