package ru.vladsaybulin.network.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import ru.vladsaybulin.network.common.AuthorizedClient
import ru.vladsaybulin.network.common.TokensHolder
import ru.vladsaybulin.network.common.interceptors.AuthInterceptor
import ru.vladsaybulin.network.common.interceptors.UserAgentInterceptor

private const val UserAgent = "Shikimori App"

@Module
@InstallIn(SingletonComponent::class)
class NetworkCommonModule {

    @Provides
    fun provideUserAgentInterceptor() = UserAgentInterceptor(UserAgent)

    @Provides
    fun provideAuthInterceptor(tokensHolder: TokensHolder) = AuthInterceptor(tokensHolder)

    @Provides
    @AuthorizedClient
    fun provideAuthorizedOkHttpClient(
        userAgentInterceptor: UserAgentInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(userAgentInterceptor)
            .addInterceptor(authInterceptor)
            .build()

    @Provides
    fun provideUnauthorizedOkHttpClient(
        userAgentInterceptor: UserAgentInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(userAgentInterceptor)
        .build()
}