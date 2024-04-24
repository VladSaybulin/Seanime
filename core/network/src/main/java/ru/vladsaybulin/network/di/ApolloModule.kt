package ru.vladsaybulin.network.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import ru.vladsaybulin.network.common.BuildConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApolloModule {
    @Provides
    @Singleton
    fun provideApolloClient(okHttpClient: OkHttpClient) = ApolloClient.Builder()
        .okHttpClient(okHttpClient)
        .serverUrl(SERVER_URL)
        .build()
}

private const val SERVER_URL = BuildConfig.BASE_URL + "/api/graphql"