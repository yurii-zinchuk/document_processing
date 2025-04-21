# Mobile App

## Description

This is the official Android mobile application for the Historical Document Processing System. It enables users to upload scanned historical documents (as ZIP archives of images) and view structured text extraction and named entity recognition (NER) results.

The application interfaces with a cloud-native backend system via HTTP APIs to perform the following:
- Request presigned URLs to securely upload ZIP files to Amazon S3.
- Upload user-provided document images.
- Poll the backend for OCR and NER task status and results.
- Display extracted entities (people, locations, dates, etc.) in a user-friendly UI.

---

## Architecture

The app is structured using a classic **Clean Architecture** approach:

- **Presentation Layer**:
  - Built with **Jetpack Compose**.
  - Uses **ViewModels** and **state holders** to manage UI state.
  - Includes screens for uploading, task status, and results viewing.

- **Domain Layer**:
  - Contains business logic.
  - Defines use cases for tasks like uploading files and fetching results.

- **Data Layer**:
  - Handles communication with the backend via **Retrofit**.
  - Manages local data persistence with **Room**.
  - Supports pagination and background work.

---

## Tools & Libraries

- **Jetpack Compose** — Declarative UI framework.
- **Retrofit** — REST client for backend communication.
- **OkHttp** — Underlying HTTP client.
- **WorkManager** — For background polling and retry mechanisms.
- **Room** — SQLite database wrapper for storing tasks and results.
- **Paging 3** — Efficient pagination of task history.
- **Hilt** — Dependency Injection.
- **Coil** — Async image loading for scanned pages and results.
- **Kotlin Coroutines** — Asynchronous processing.

---

## Features

- Select multiple images and create a ZIP archive.
- Request and use a presigned S3 upload URL.
- Upload scanned documents securely to S3.
- Poll task status in background using WorkManager.
- Display structured NER results.
- View task history with pagination and persistent storage.

---

## Setup

1. Clone this repository.
2. Open the `mobile/` directory in Android Studio.
3. Add the required `BASE_URL` and permissions in your `local.properties` or build config.
4. Run the app on an emulator or device (API 26+).

---

## Folder Structure

- `presentation/` — UI layer (screens, components, ViewModels).
- `domain/` — Use cases and domain models.
- `data/` — Network layer, Room DB, repositories.
- `utils/` — Utility code used throughout the entire app.

---

For backend configuration, see [`backend/README.md`](../backend/README.md).

