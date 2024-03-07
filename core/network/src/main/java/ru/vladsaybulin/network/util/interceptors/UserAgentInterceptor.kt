package ru.vladsaybulin.network.util.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class UserAgentInterceptor(private val userAgent: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.request().newBuilder()
            .addHeader("User-Agent", userAgent)
            .build()
            .let { chain.proceed(it) }
}