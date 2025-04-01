package com.example.scandoc.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.scandoc.data.room.AppDatabase
import com.example.scandoc.data.room.mappers.MapperDocumentSetDomainToRoom
import com.example.scandoc.data.room.mappers.MapperDocumentSetRoomToDomain
import com.example.scandoc.domain.models.DocumentSet
import com.example.scandoc.domain.repositories.DocumentSetsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class DocumentSetsRepositoryImpl @Inject constructor(
    private val mapperDocumentSetDomainToRoom: MapperDocumentSetDomainToRoom,
    private val mapperDocumentSetRoomToDomain: MapperDocumentSetRoomToDomain,
    private val database: AppDatabase,
) : DocumentSetsRepository {
    override suspend fun deleteDocumentSet(uuid: UUID) {
        database.documentSetDao().deleteDocumentSet(uuid.toString())
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

    override fun getAllDocumentSets(): Flow<PagingData<DocumentSet>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { database.documentSetDao().getAllDocumentSets() }
        ).flow
            .map { pagingData ->
                pagingData.map { mapperDocumentSetRoomToDomain.map(it) }
            }
    }

    private companion object {
        private const val PAGE_SIZE = 20
    }
}
