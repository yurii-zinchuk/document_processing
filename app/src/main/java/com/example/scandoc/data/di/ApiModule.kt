package com.example.scandoc.data.di

import com.example.scandoc.data.network.api.ProcessingApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    fun provideProcessingApi(
        retrofit: Retrofit,
    ): ProcessingApi = retrofit.create(ProcessingApi::class.java)

}
