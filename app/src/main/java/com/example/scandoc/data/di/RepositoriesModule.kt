package com.example.scandoc.data.di

import com.example.scandoc.data.repositories.DocumentSetsRepositoryImpl
import com.example.scandoc.data.repositories.ImagesRepositoryImpl
import com.example.scandoc.data.repositories.PDFRepositoryImpl
import com.example.scandoc.domain.repositories.DocumentSetsRepository
import com.example.scandoc.domain.repositories.ImagesRepository
import com.example.scandoc.domain.repositories.PDFRepository
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
    abstract fun bindPDFRepository(repositoryImpl: PDFRepositoryImpl): PDFRepository
}
