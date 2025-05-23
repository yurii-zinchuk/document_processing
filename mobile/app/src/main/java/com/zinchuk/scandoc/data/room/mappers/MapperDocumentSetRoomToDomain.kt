package com.zinchuk.scandoc.data.room.mappers

import com.zinchuk.scandoc.data.room.models.RoomDocumentSet
import com.zinchuk.scandoc.domain.mappers.DataMapper
import com.zinchuk.scandoc.domain.models.DocumentSet
import java.util.UUID
import javax.inject.Inject

class MapperDocumentSetRoomToDomain
    @Inject
    constructor() : DataMapper<RoomDocumentSet, DocumentSet> {
        override fun map(data: RoomDocumentSet): DocumentSet {
            return data.run {
                DocumentSet(
                    uuid = UUID.fromString(uuid),
                    name = name,
                    createdAt = data.timestamp,
                )
            }
        }
    }
