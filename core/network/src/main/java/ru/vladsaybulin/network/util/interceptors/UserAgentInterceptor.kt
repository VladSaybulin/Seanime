package ru.vladsaybulin.network.util.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import ru.vladsaybulin.core.auth.BuildConfig
import javax.inject.Inject

class UserAgentInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.request().newBuilder()
            .addHeader("User-Agent", BuildConfig.SHIKIMORI_USER_AGENT)
            .build()
            .let { chain.proceed(it) }
}