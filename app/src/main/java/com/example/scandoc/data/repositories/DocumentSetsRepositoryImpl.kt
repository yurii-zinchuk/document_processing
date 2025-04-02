package com.example.scandoc.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.scandoc.data.network.api.ProcessingApi
import com.example.scandoc.data.network.mappers.MapperFileToMultipart
import com.example.scandoc.data.network.mappers.MapperProcessingResultResponseToDomain
import com.example.scandoc.data.network.models.TaskStatusResponse
import com.example.scandoc.data.room.AppDatabase
import com.example.scandoc.data.room.mappers.MapperDocumentSetDomainToRoom
import com.example.scandoc.data.room.mappers.MapperDocumentSetRoomToDomain
import com.example.scandoc.data.storage.InternalStorage
import com.example.scandoc.domain.models.DocumentSet
import com.example.scandoc.domain.models.ProcessedData
import com.example.scandoc.domain.repositories.DocumentSetsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.util.UUID
import javax.inject.Inject

class DocumentSetsRepositoryImpl @Inject constructor(
    private val mapperDocumentSetDomainToRoom: MapperDocumentSetDomainToRoom,
    private val mapperDocumentSetRoomToDomain: MapperDocumentSetRoomToDomain,
    private val mapperProcessingResultResponseToDomain: MapperProcessingResultResponseToDomain,
    private val mapperFileToMultipart: MapperFileToMultipart,
    private val processingApi: ProcessingApi,
    private val internalStorage: InternalStorage,
    private val database: AppDatabase,
) : DocumentSetsRepository {

    override suspend fun deleteDocumentSet(uuid: UUID) {
        database.documentSetDao().deleteDocumentSet(uuid.toString())
        getInternalProcessedDataDirectory(uuid)
            .takeIf { it.exists() && it.isDirectory }
            ?.deleteRecursively()
    }

    override suspend fun saveDocumentSet(documentSet: DocumentSet) {
        database.documentSetDao().addDocumentSet(
            mapperDocumentSetDomainToRoom.map(documentSet)
        )
    }

    override suspend fun getDocumentSet(uuid: UUID): DocumentSet {
        return database.documentSetDao().getDocumentSetByUUID(uuid.toString())
            .let { mapperDocumentSetRoomToDomain.map(it) }
    }

    override suspend fun processDocumentSet(pdfFile: File): ProcessedData? {
        try {
            val uploadResponse =
                mapperFileToMultipart.map(pdfFile)
                    .let { processingApi.uploadPdf(it) }
                    .also { if (!it.isSuccessful) throw ProcessingException() }

            val taskId = uploadResponse.body()?.taskId ?: throw ProcessingException()

            repeat(POLLING_RETRIES) {
                delay(POLLING_DELAY)

                val taskStatusResponse = processingApi
                    .getTaskStatus(taskId)
                    .also { if (!it.isSuccessful) throw ProcessingException() }

                taskStatusResponse
                    .body()
                    ?.let { responseBody ->
                        when(responseBody.status) {
                            TaskStatusResponse.TaskStatus.DONE -> return responseBody.result
                                ?.let { result -> mapperProcessingResultResponseToDomain.map(result) }
                            TaskStatusResponse.TaskStatus.ERROR -> throw ProcessingException()
                            else -> {}
                        }
                    }
            }
        } catch (_: ProcessingException) {
            return null
        }

        return null
    }

    override fun getAllDocumentSets(): Flow<PagingData<DocumentSet>> {
        return Pager(
            config = PagingConfig(
                pageSize = PHOTOS_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { database.documentSetDao().getAllDocumentSets() }
        ).flow
            .map { pagingData ->
                pagingData.map { mapperDocumentSetRoomToDomain.map(it) }
            }
    }

    override fun getProcessedData(uuid: UUID): ProcessedData {
        val text = getProcessedTextFile(uuid)
                .takeIf { it.exists() }
                ?.readText()
        val entities = getProcessedEntitiesFile(uuid)
            .takeIf { it.exists() }
            ?.readText()
            ?.split(ENTITIES_SEPARATOR)

        return ProcessedData(text, entities)
    }

    override fun saveProcessedData(uuid: UUID, data: ProcessedData) {
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

    private companion object {
        private const val PROCESSED_TEXT_FILE_NAME = "text.txt"
        private const val PROCESSED_ENTITIES_FILE_NAME = "entities.txt"
        private const val PROCESSED_DATA_DIRECTORY = "processed"
        private const val ENTITIES_SEPARATOR = "\n"
        private const val PHOTOS_PAGE_SIZE = 20
        private const val POLLING_RETRIES = 300
        private const val POLLING_DELAY = 10_000L
    }

    private inner class ProcessingException : Exception()
}
