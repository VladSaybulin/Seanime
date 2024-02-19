package ru.vladsaybulin.network.common.interceptors

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ru.vladsaybulin.network.common.TokensHolder

class AuthInterceptor(private val tokensHolder: TokensHolder) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        val response = tokensHolder.getAccessToken()
            ?.let { token -> interceptWithAccessToken(chain, token) }
            ?: return@runBlocking interceptWithoutAccessToken(chain)

        return@runBlocking if (response.code() == INVALID_ACCESS_TOKEN_CODE) {
            tokensHolder.refreshAccessToken()
                ?.let { token -> interceptWithAccessToken(chain, token) }
                ?: interceptWithoutAccessToken(chain)
        } else response
    }

    private fun interceptWithAccessToken(chain: Interceptor.Chain, accessToken: String): Response =
        chain.request().newBuilder()
            .addHeader("Authorization", "Bearer: $accessToken")
            .build()
            .let { chain.proceed(it) }

    private fun interceptWithoutAccessToken(chain: Interceptor.Chain): Response =
        chain.proceed(chain.request())
}

private const val INVALID_ACCESS_TOKEN_CODE = 401