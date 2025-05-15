package com.zinchuk.scandoc.data.di

import com.zinchuk.scandoc.data.storage.InternalStorage
import com.zinchuk.scandoc.data.storage.InternalStorageImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {
    @Binds
    abstract fun bindInternalStorage(storageImpl: InternalStorageImpl): InternalStorage
}
