package com.example.scandoc.data.di

import androidx.hilt.work.HiltWorkerFactory
import androidx.work.WorkerFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class WorkersModule {
    @Binds
    abstract fun bindWorkerFactory(
        factory: HiltWorkerFactory
    ): WorkerFactory
}
