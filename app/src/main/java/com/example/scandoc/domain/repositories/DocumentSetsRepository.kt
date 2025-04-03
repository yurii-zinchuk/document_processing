package com.example.scandoc.domain.repositories

import androidx.paging.PagingData
import androidx.work.WorkInfo
import com.example.scandoc.domain.models.DocumentSet
import com.example.scandoc.domain.models.ProcessedData
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.util.UUID

interface DocumentSetsRepository {
    suspend fun deleteDocumentSet(uuid: UUID)

    suspend fun saveDocumentSet(documentSet: DocumentSet)

    suspend fun getDocumentSet(uuid: UUID): DocumentSet

    suspend fun processDocumentSet(
        uuid: UUID,
        pdfFile: File,
    ): UUID

    fun getAllDocumentSets(): Flow<PagingData<DocumentSet>>

    fun getProcessedData(uuid: UUID): ProcessedData

    fun getDocumentSetWorkInfo(uuid: UUID): Flow<List<WorkInfo>>

    fun cancelProcessingWork(uuid: UUID)
}
