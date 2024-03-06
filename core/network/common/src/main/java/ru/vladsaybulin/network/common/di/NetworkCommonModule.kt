package ru.vladsaybulin.network.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.vladsaybulin.network.common.ShikiAuthenticator
import ru.vladsaybulin.network.common.TokensHolder
import ru.vladsaybulin.network.common.interceptors.AuthorizationInterceptor
import ru.vladsaybulin.network.common.interceptors.UserAgentInterceptor
import javax.inject.Singleton

private const val UserAgent = "Shikimori App"

@Module
@InstallIn(SingletonComponent::class)
class NetworkCommonModule {

    @Provides
    fun provideUserAgentInterceptor(): UserAgentInterceptor =
        UserAgentInterceptor(UserAgent)

    @Provides
    fun provideAuthenticator(tokensHolder: TokensHolder): ShikiAuthenticator =
        ShikiAuthenticator(tokensHolder)

    @Provides
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    @AuthorizedClient
    fun provideAuthorizedOkHttpClient(
        userAgentInterceptor: UserAgentInterceptor,
        authorizationInterceptor: AuthorizationInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        authenticator: ShikiAuthenticator
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(userAgentInterceptor)
            .addInterceptor(authorizationInterceptor)
            .authenticator(authenticator)
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideUnauthorizedOkHttpClient(
        userAgentInterceptor: UserAgentInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(userAgentInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()
}