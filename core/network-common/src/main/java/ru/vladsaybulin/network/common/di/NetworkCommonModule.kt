package ru.vladsaybulin.network.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import ru.vladsaybulin.network.common.ShikiAuthenticator
import ru.vladsaybulin.network.common.TokensHolder
import ru.vladsaybulin.network.common.interceptors.AuthorizationInterceptor
import ru.vladsaybulin.network.common.interceptors.UserAgentInterceptor

private const val UserAgent = "Shikimori App"

@Module
@InstallIn(SingletonComponent::class)
class NetworkCommonModule {

    @Provides
    fun provideUserAgentInterceptor(): UserAgentInterceptor =
        UserAgentInterceptor(UserAgent)

    @Provides
    fun provideAuthInterceptor(tokensHolder: TokensHolder): AuthorizationInterceptor =
        AuthorizationInterceptor(tokensHolder)

    @Provides
    fun provideAuthenticator(tokensHolder: TokensHolder): ShikiAuthenticator =
        ShikiAuthenticator(tokensHolder)

    @Provides
    @AuthorizedClient
    fun provideAuthorizedOkHttpClient(
        userAgentInterceptor: UserAgentInterceptor,
        authorizationInterceptor: AuthorizationInterceptor,
        authenticator: ShikiAuthenticator,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(userAgentInterceptor)
            .addInterceptor(authorizationInterceptor)
            .authenticator(authenticator)
            .build()

    @Provides
    fun provideUnauthorizedOkHttpClient(
        userAgentInterceptor: UserAgentInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(userAgentInterceptor)
        .build()
}