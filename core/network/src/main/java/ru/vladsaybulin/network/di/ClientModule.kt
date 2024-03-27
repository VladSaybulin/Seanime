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
    fun provideUnauthorizedOkHttpClient(
        userAgentInterceptor: UserAgentInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(userAgentInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    @AuthorizedClient
    fun provideAuthorizedOkHttpClient(
        unauthorizedOkHttpClient: OkHttpClient,
        authorizationInterceptor: AuthorizationInterceptor,
        authenticator: ShikimoriAuthenticator
    ): OkHttpClient =
        unauthorizedOkHttpClient.newBuilder()
            .addInterceptor(authorizationInterceptor)
            .authenticator(authenticator)
            .build()
}