package com.example.scandoc.data.workers

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.scandoc.R
import com.example.scandoc.data.network.api.ProcessingApi
import com.example.scandoc.data.network.mappers.MapperFileToMultipart
import com.example.scandoc.data.network.mappers.MapperProcessingResultResponseToDomain
import com.example.scandoc.data.network.models.TaskStatusResponse
import com.example.scandoc.data.storage.InternalStorage
import com.example.scandoc.domain.models.ProcessedData
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import java.io.File
import java.util.UUID

@HiltWorker
class ProcessingWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val mapperProcessingResultResponseToDomain: MapperProcessingResultResponseToDomain,
    private val mapperFileToMultipart: MapperFileToMultipart,
    private val processingApi: ProcessingApi,
    private val internalStorage: InternalStorage,
) : CoroutineWorker(
    context,
    workerParameters,
) {
    private var documentSetUUID: UUID? = null

    override suspend fun doWork(): Result {
        try {
            val pdfFile = File(inputData.getString(FILE_KEY) ?: return Result.failure())
            documentSetUUID = inputData.getString(UUID_KEY)?.let { UUID.fromString(it) }
            val uploadResponse =
                mapperFileToMultipart.map(pdfFile)
                    .let { processingApi.uploadPdf(it) }
                    .also { if (!it.isSuccessful) return Result.failure() }

            val taskId = uploadResponse.body()?.taskId ?: return Result.failure()

            repeat(POLLING_RETRIES) {
                delay(POLLING_DELAY)

                val taskStatusResponse = processingApi
                    .getTaskStatus(taskId)
                    .also { if (!it.isSuccessful) return Result.failure() }

                taskStatusResponse
                    .body()
                    ?.let { responseBody ->
                        when (responseBody.status) {
                            TaskStatusResponse.TaskStatus.DONE -> {
                                val result = responseBody.result
                                    ?.let { mapperProcessingResultResponseToDomain.map(it) }
                                    ?: return Result.failure()

                                documentSetUUID?.let { saveProcessedData(it, result) }

                                return Result.success()
                            }

                            TaskStatusResponse.TaskStatus.ERROR -> Result.failure()

                            else -> {}
                        }
                    }
            }
        } catch (_: Throwable) {
            return Result.failure()
        }

        return Result.failure()
    }

    private fun saveProcessedData(uuid: UUID, data: ProcessedData) {
        data.run {
            if (text != null) {
                getProcessedTextFile(uuid)
                    .also { if (!it.exists()) it.createNewFile() }
                    .takeIf { it.exists() }
                    ?.writeText(text)
            }
            entities?.joinToString(ENTITIES_SEPARATOR)?.let { entitiesText ->
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

    companion object {
        const val FILE_KEY = "PDF_FILE_NAME"
        const val UUID_KEY = "UUID_STRING"

        private const val PROCESSED_TEXT_FILE_NAME = "text.txt"
        private const val PROCESSED_ENTITIES_FILE_NAME = "entities.txt"
        private const val PROCESSED_DATA_DIRECTORY = "processed"
        private const val ENTITIES_SEPARATOR = "\n"
        private const val POLLING_RETRIES = 300
        private const val POLLING_DELAY = 10_000L
    }

}
