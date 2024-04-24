package ru.vladsaybulin.network.util.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import ru.vladsaybulin.core.auth.ShikimoriAuthorization
import ru.vladsaybulin.network.util.addAuthorizationHeader
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(
    private val authorization: ShikimoriAuthorization
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = authorization.getFreshAccessToken()
            ?: return chain.proceed(chain.request())
        val request = chain.request().newBuilder()
            .addAuthorizationHeader(accessToken)
            .build()
        return chain.proceed(request)
    }
}