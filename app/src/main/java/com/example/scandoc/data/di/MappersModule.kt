package com.example.scandoc.data.di

import com.example.scandoc.data.room.mappers.MapperDocumentSetDomainToRoom
import com.example.scandoc.data.room.mappers.MapperDocumentSetRoomToDomain
import com.example.scandoc.data.room.models.RoomDocumentSet
import com.example.scandoc.domain.mappers.DataMapper
import com.example.scandoc.domain.models.DocumentSet
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MappersModule {
    @Binds
    abstract fun bindMapperDocumentSetRoomToDomain(
        mapper: MapperDocumentSetRoomToDomain
    ): DataMapper<RoomDocumentSet, DocumentSet>

    @Binds
    abstract fun bindMapperDocumentSetDomainToRoom(
        mapper: MapperDocumentSetDomainToRoom
    ): DataMapper<DocumentSet, RoomDocumentSet>
}
