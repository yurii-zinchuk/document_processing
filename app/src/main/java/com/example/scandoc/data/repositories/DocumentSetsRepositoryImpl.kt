package com.example.scandoc.data.repositories

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.scandoc.data.room.AppDatabase
import com.example.scandoc.data.room.mappers.MapperDocumentSetDomainToRoom
import com.example.scandoc.data.room.mappers.MapperDocumentSetRoomToDomain
import com.example.scandoc.data.storage.InternalStorage
import com.example.scandoc.data.workers.ProcessingWorker
import com.example.scandoc.data.workers.ProcessingWorker.Companion.wrapWorkData
import com.example.scandoc.domain.models.DocumentSet
import com.example.scandoc.domain.models.ProcessedData
import com.example.scandoc.domain.repositories.DocumentSetsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.util.UUID
import javax.inject.Inject

class DocumentSetsRepositoryImpl @Inject constructor(
    private val mapperDocumentSetDomainToRoom: MapperDocumentSetDomainToRoom,
    private val mapperDocumentSetRoomToDomain: MapperDocumentSetRoomToDomain,
    private val internalStorage: InternalStorage,
    private val database: AppDatabase,
    @ApplicationContext private val context: Context,
) : DocumentSetsRepository {

    override suspend fun deleteDocumentSet(uuid: UUID) {
        cancelProcessingWork(uuid)
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

    override suspend fun processDocumentSet(uuid: UUID, pdfFile: File): UUID {
        val input = wrapWorkData(pdfFile, uuid)
        val constraints =
            Constraints
                .Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        val request = OneTimeWorkRequestBuilder<ProcessingWorker>()
            .setInputData(input)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            uniqueWorkName = uuid.toString(),
            existingWorkPolicy = ExistingWorkPolicy.REPLACE,
            request = request,
        )

        return request.id
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

    override fun getDocumentSetWorkInfo(uuid: UUID): Flow<List<WorkInfo>> {
        return WorkManager.getInstance(context).getWorkInfosForUniqueWorkFlow(uuid.toString())
    }

    override fun cancelProcessingWork(uuid: UUID) {
        WorkManager.getInstance(context).cancelUniqueWork(uuid.toString())
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
    }
}
