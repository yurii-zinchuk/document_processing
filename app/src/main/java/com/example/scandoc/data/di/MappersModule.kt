package com.example.scandoc.data.di

import com.example.scandoc.data.network.mappers.MapperFileToRequestBody
import com.example.scandoc.data.network.mappers.MapperProcessingResultResponseToDomain
import com.example.scandoc.data.network.models.ProcessingResultResponse
import com.example.scandoc.data.room.mappers.MapperDocumentSetDomainToRoom
import com.example.scandoc.data.room.mappers.MapperDocumentSetRoomToDomain
import com.example.scandoc.data.room.models.RoomDocumentSet
import com.example.scandoc.domain.mappers.DataMapper
import com.example.scandoc.domain.models.DocumentSet
import com.example.scandoc.domain.models.ProcessedData
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.RequestBody
import java.io.File

@Module
@InstallIn(SingletonComponent::class)
abstract class MappersModule {
    @Binds
    abstract fun bindMapperDocumentSetRoomToDomain(mapper: MapperDocumentSetRoomToDomain): DataMapper<RoomDocumentSet, DocumentSet>

    @Binds
    abstract fun bindMapperDocumentSetDomainToRoom(mapper: MapperDocumentSetDomainToRoom): DataMapper<DocumentSet, RoomDocumentSet>

    @Binds
    abstract fun bindMapperFileToMultipart(mapper: MapperFileToRequestBody): DataMapper<File, RequestBody>

    @Binds
    abstract fun bindMapperProcessingResultResponseToDomain(
        mapper: MapperProcessingResultResponseToDomain,
    ): DataMapper<ProcessingResultResponse, ProcessedData>
}
