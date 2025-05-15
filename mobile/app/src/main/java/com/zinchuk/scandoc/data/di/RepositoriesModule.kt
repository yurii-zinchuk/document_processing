package com.zinchuk.scandoc.data.di

import com.zinchuk.scandoc.data.repositories.DocumentSetsRepositoryImpl
import com.zinchuk.scandoc.data.repositories.ImagesRepositoryImpl
import com.zinchuk.scandoc.data.repositories.ZIPRepositoryImpl
import com.zinchuk.scandoc.domain.repositories.DocumentSetsRepository
import com.zinchuk.scandoc.domain.repositories.ImagesRepository
import com.zinchuk.scandoc.domain.repositories.ZIPRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {
    @Binds
    abstract fun bindImagesRepository(repositoryImpl: ImagesRepositoryImpl): ImagesRepository

    @Binds
    abstract fun bindDocumentSetsRepository(repositoryImpl: DocumentSetsRepositoryImpl): DocumentSetsRepository

    @Binds
    abstract fun bindZIPRepository(repositoryImpl: ZIPRepositoryImpl): ZIPRepository
}
