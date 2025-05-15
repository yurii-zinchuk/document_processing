package com.zinchuk.scandoc.presentation.di

import com.zinchuk.scandoc.domain.mappers.SuspendDataMapper
import com.zinchuk.scandoc.domain.models.DocumentSet
import com.zinchuk.scandoc.presentation.mappers.MapperDocumentSetDomainToUI
import com.zinchuk.scandoc.presentation.models.DocumentSetItemData
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PresentationModule {
    @Binds
    abstract fun bindMapperDocumentSetDomainToUI(mapper: MapperDocumentSetDomainToUI): SuspendDataMapper<DocumentSet, DocumentSetItemData>
}
