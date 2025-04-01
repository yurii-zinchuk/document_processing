package com.example.scandoc.data.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(
    includes = [
        MappersModule::class,
        RepositoriesModule::class,
        RoomModule::class,
    ]
)
@InstallIn(SingletonComponent::class)
class DataModule
