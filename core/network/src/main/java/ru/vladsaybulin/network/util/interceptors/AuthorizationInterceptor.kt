package ru.vladsaybulin.network.util.interceptors

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ru.vladsaybulin.network.TokensHolder
import ru.vladsaybulin.network.util.addAuthorizationHeader
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(private val tokensHolder: TokensHolder) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        tokensHolder.getAccessToken()
            ?.let { token -> interceptWithAccessToken(chain, token) }
            ?: return@runBlocking interceptWithoutAccessToken(chain)
    }

    private fun interceptWithAccessToken(chain: Interceptor.Chain, accessToken: String): Response =
        chain.request().newBuilder()
            .addAuthorizationHeader(accessToken)
            .build()
            .let { chain.proceed(it) }

    private fun interceptWithoutAccessToken(chain: Interceptor.Chain): Response =
        chain.proceed(chain.request())
}