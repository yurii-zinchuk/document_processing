package com.example.scandoc.presentation.di

import com.example.scandoc.domain.mappers.SuspendDataMapper
import com.example.scandoc.domain.models.DocumentSet
import com.example.scandoc.presentation.mappers.MapperDocumentSetDomainToUI
import com.example.scandoc.presentation.models.DocumentSetItemData
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PresentationModule {
    @Binds
    abstract fun bindMapperDocumentSetDomainToUI(
        mapper: MapperDocumentSetDomainToUI,
    ): SuspendDataMapper<DocumentSet, DocumentSetItemData>
}
