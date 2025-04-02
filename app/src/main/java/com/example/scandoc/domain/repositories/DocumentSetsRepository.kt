package com.example.scandoc.domain.repositories

import androidx.paging.PagingData
import com.example.scandoc.domain.models.DocumentSet
import com.example.scandoc.domain.models.ProcessedData
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.util.UUID

interface DocumentSetsRepository {

    suspend fun deleteDocumentSet(uuid: UUID)

    suspend fun saveDocumentSet(documentSet: DocumentSet)

    suspend fun getDocumentSet(uuid: UUID): DocumentSet

    suspend fun processDocumentSet(pdfFile: File): ProcessedData?

    fun getAllDocumentSets(): Flow<PagingData<DocumentSet>>

    fun getProcessedData(uuid: UUID): ProcessedData

    fun saveProcessedData(uuid: UUID, data: ProcessedData)

}
