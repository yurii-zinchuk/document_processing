package com.example.scandoc.domain.repositories

import androidx.paging.PagingData
import com.example.scandoc.domain.models.DocumentSet
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface DocumentSetsRepository {

    suspend fun deleteDocumentSet(uuid: UUID)

    suspend fun saveDocumentSet(documentSet: DocumentSet)

    suspend fun getDocumentSet(uuid: UUID): DocumentSet

    fun getAllDocumentSets(): Flow<PagingData<DocumentSet>>

}
