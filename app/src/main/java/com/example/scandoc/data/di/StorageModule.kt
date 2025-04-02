package com.example.scandoc.data.di

import com.example.scandoc.data.storage.InternalStorage
import com.example.scandoc.data.storage.InternalStorageImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {
    @Binds
    abstract fun bindInternalStorage(
        storageImpl: InternalStorageImpl
    ): InternalStorage
}
