package com.zinchuk.scandoc.data.room.mappers

import com.zinchuk.scandoc.data.room.models.RoomDocumentSet
import com.zinchuk.scandoc.domain.mappers.DataMapper
import com.zinchuk.scandoc.domain.models.DocumentSet
import javax.inject.Inject

class MapperDocumentSetDomainToRoom
    @Inject
    constructor() : DataMapper<DocumentSet, RoomDocumentSet> {
        override fun map(data: DocumentSet): RoomDocumentSet {
            return data.run {
                RoomDocumentSet(
                    uuid = uuid.toString(),
                    name = name,
                    timestamp = data.createdAt,
                )
            }
        }
    }
