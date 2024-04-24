package ru.vladsaybulin.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.vladsaybulin.network.util.ShikimoriAuthenticator
import ru.vladsaybulin.network.util.interceptors.AuthorizationInterceptor
import ru.vladsaybulin.network.util.interceptors.UserAgentInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ClientModule {

    @Provides
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        userAgentInterceptor: UserAgentInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        authorizationInterceptor: AuthorizationInterceptor,
        authenticator: ShikimoriAuthenticator
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(userAgentInterceptor)
            .addInterceptor(authorizationInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(authenticator)
            .build()
}