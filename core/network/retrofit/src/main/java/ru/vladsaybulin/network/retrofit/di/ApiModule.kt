package ru.vladsaybulin.network.retrofit.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import ru.vladsaybulin.network.retrofit.ShikiUnauthorizedApi

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    fun provideUnauthorizedApi(retrofit: Retrofit): ShikiUnauthorizedApi = retrofit.create()

}