package com.example.scandoc.data.room.mappers

import com.example.scandoc.data.room.models.RoomDocumentSet
import com.example.scandoc.domain.mappers.DataMapper
import com.example.scandoc.domain.models.DocumentSet
import javax.inject.Inject

class MapperDocumentSetDomainToRoom @Inject constructor() : DataMapper<DocumentSet, RoomDocumentSet> {
    override fun map(data: DocumentSet): RoomDocumentSet {
        return data.run {
            RoomDocumentSet(
                uuid = uuid.toString(),
                name = name,
                timestamp = data.createdAt,
                processingStatus = data.processingStatus
            )
        }
    }
}
