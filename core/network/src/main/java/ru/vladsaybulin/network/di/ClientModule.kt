package ru.vladsaybulin.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.vladsaybulin.network.TokensHolder
import ru.vladsaybulin.network.common.BuildConfig
import ru.vladsaybulin.network.util.ShikiAuthenticator
import ru.vladsaybulin.network.util.interceptors.AuthorizationInterceptor
import ru.vladsaybulin.network.util.interceptors.UserAgentInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ClientModule {

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
            .authenticator(authenticator)
            .addInterceptor(userAgentInterceptor)
            .addInterceptor(authorizationInterceptor)
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

private const val UserAgent = BuildConfig.SHIKIMORI_USER_AGENT