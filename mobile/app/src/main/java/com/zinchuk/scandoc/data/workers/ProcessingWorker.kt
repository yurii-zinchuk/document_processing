package com.zinchuk.scandoc.data.workers

import android.content.Context
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.Q
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.zinchuk.scandoc.data.network.api.ProcessingApi
import com.zinchuk.scandoc.data.network.mappers.MapperFileToRequestBody
import com.zinchuk.scandoc.data.network.mappers.MapperProcessingResultResponseToDomain
import com.zinchuk.scandoc.data.network.models.CreateTaskResponse
import com.zinchuk.scandoc.data.network.models.TaskStatusResponse
import com.zinchuk.scandoc.data.room.AppDatabase
import com.zinchuk.scandoc.data.storage.InternalStorage
import com.zinchuk.scandoc.data.workers.ProcessingNotificationManager.Companion.PROGRESS_NOTIFICATION_ID
import com.zinchuk.scandoc.domain.models.ProcessedData
import com.zinchuk.scandoc.utils.noop
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import java.io.File
import java.util.UUID

@HiltWorker
class ProcessingWorker
    @AssistedInject
    constructor(
        @Assisted private val context: Context,
        @Assisted workerParameters: WorkerParameters,
        private val mapperProcessingResultResponseToDomain: MapperProcessingResultResponseToDomain,
        private val mapperFileToRequestBody: MapperFileToRequestBody,
        private val processingApi: ProcessingApi,
        private val internalStorage: InternalStorage,
        private val notificationManager: ProcessingNotificationManager,
        private val appDatabase: AppDatabase,
    ) : CoroutineWorker(
            context,
            workerParameters,
        ) {
        private var documentSetUUID = unwrapWorkData(inputData).uuid

        override suspend fun doWork(): Result =
            try {
                notificationManager.createNotificationChannel()
                setForeground(getForegroundInfo())

                val zipFile = unwrapWorkData(inputData).file
                val taskResponse = createProcessingTask()
                val (taskId, uploadURL) = taskResponse.run { taskId to uploadURL }

                uploadZIPFile(
                    url = uploadURL,
                    file = zipFile,
                )

                repeat(POLLING_RETRIES) {
                    delay(POLLING_DELAY)

                    getTaskStatusResponse(taskId).let {
                        when (it.status) {
                            TaskStatusResponse.TaskStatus.DONE -> onTaskDone(it)
                            TaskStatusResponse.TaskStatus.ERROR -> onProcessingFailed()
                            else -> noop()
                        }
                    }
                }
                onProcessingFailed()
            } catch (_: ProcessingSuccess) {
                Result.success()
            } catch (_: ProcessingFailure) {
                Result.failure()
            } catch (_: Throwable) {
                // Every other exception in processing
                Result.failure()
            }

        override suspend fun getForegroundInfo(): ForegroundInfo =
            if (SDK_INT >= Q) {
                ForegroundInfo(
                    PROGRESS_NOTIFICATION_ID + documentSetUUID.hashCode(),
                    notificationManager.getNotification(getDocumentSetName(documentSetUUID)),
                    FOREGROUND_SERVICE_TYPE_DATA_SYNC,
                )
            } else {
                ForegroundInfo(
                    PROGRESS_NOTIFICATION_ID + documentSetUUID.hashCode(),
                    notificationManager.getNotification(getDocumentSetName(documentSetUUID)),
                )
            }

        private suspend fun uploadZIPFile(
            url: String,
            file: File,
        ) {
            mapperFileToRequestBody.map(file)
                .let { processingApi.uploadZIPFile(url, it) }
                .also { if (!it.isSuccessful) onProcessingFailed() }
        }

        private suspend fun createProcessingTask(): CreateTaskResponse {
            return processingApi
                .getTaskInfo()
                .also { if (!it.isSuccessful) onProcessingFailed() }
                .body()
                ?: onProcessingFailed()
        }

        private suspend fun getTaskStatusResponse(taskId: String): TaskStatusResponse {
            return processingApi
                .getTaskStatus(taskId)
                .also { if (!it.isSuccessful) onProcessingFailed() }
                .body()
                ?: onProcessingFailed()
        }

        private suspend fun onTaskDone(statusResponse: TaskStatusResponse) {
            val result =
                statusResponse.result
                    ?.let { mapperProcessingResultResponseToDomain.map(it) }
                    ?: onProcessingFailed()

            saveProcessedData(documentSetUUID, result)
            onProcessingSuccess()
        }

        private fun saveProcessedData(
            uuid: UUID,
            data: ProcessedData,
        ) {
            data.run {
                if (text != null) {
                    getProcessedTextFile(uuid)
                        .also { if (!it.exists()) it.createNewFile() }
                        .takeIf { it.exists() }
                        ?.writeText(text)
                }
                Gson().toJson(entities)?.let { entitiesText ->
                    getProcessedEntitiesFile(uuid)
                        .also { if (!it.exists()) it.createNewFile() }
                        .takeIf { it.exists() }
                        ?.writeText(entitiesText)
                }
            }
        }

        private fun getInternalProcessedDataDirectory(uuid: UUID): File =
            File(
                internalStorage.getDirectory(uuid),
                PROCESSED_DATA_DIRECTORY,
            ).also { if (!it.exists()) it.mkdirs() }

        private fun getProcessedTextFile(uuid: UUID): File =
            File(
                getInternalProcessedDataDirectory(uuid),
                PROCESSED_TEXT_FILE_NAME,
            )

        private fun getProcessedEntitiesFile(uuid: UUID): File =
            File(
                getInternalProcessedDataDirectory(uuid),
                PROCESSED_ENTITIES_FILE_NAME,
            )

        private suspend fun onProcessingFailed(): Nothing {
            notificationManager.sendFailureNotification(
                getDocumentSetName(documentSetUUID),
                documentSetUUID.toString(),
            )
            throw ProcessingFailure()
        }

        private suspend fun onProcessingSuccess(): Nothing {
            notificationManager.sendSuccessNotification(
                getDocumentSetName(documentSetUUID),
                documentSetUUID.toString(),
            )
            throw ProcessingSuccess()
        }

        private suspend fun getDocumentSetName(uuid: UUID): String {
            return appDatabase.documentSetDao().getDocumentSetByUUID(uuid.toString()).name
        }

        companion object {
            private const val FILE_KEY = "ZIP_FILE_NAME"
            private const val UUID_KEY = "UUID_STRING"

            private const val PROCESSED_TEXT_FILE_NAME = "text.txt"
            private const val PROCESSED_ENTITIES_FILE_NAME = "entities.txt"
            private const val PROCESSED_DATA_DIRECTORY = "processed"

            private const val POLLING_RETRIES = 300
            private const val POLLING_DELAY = 10_000L

            fun wrapWorkData(
                zipFilePath: File,
                documentSetUUID: UUID,
            ): Data =
                workDataOf(
                    FILE_KEY to zipFilePath.absolutePath,
                    UUID_KEY to documentSetUUID.toString(),
                )

            private fun unwrapWorkData(inputData: Data): WorkerInputData =
                WorkerInputData(
                    File(inputData.getString(FILE_KEY)!!),
                    UUID.fromString(inputData.getString(UUID_KEY)!!),
                )
        }
    }

private class ProcessingFailure : Exception()

private class ProcessingSuccess : Exception()

private data class WorkerInputData(
    val file: File,
    val uuid: UUID,
)
