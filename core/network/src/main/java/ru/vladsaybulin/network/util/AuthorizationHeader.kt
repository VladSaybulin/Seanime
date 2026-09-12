/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vladsaybulin.network.util

import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.api.Operation
import okhttp3.Request

private const val AUTHORIZED_CALL_HEADER_NAME = "X-Authorized-Call"
private const val AUTHORIZATION_HEADER_NAME = "Authorization"
private const val AUTHORIZATION_TYPE_BEARER = "Bearer"

/**
 * Header that is added to Retrofit requests that require authorization.
 * This header is used to distinguish between public and authorized calls.
 */
const val AUTHORIZED_CALL_HEADER = "$AUTHORIZED_CALL_HEADER_NAME: true"

/**
 * Marks the Apollo call as an authorized call by adding a custom header.
 * This header is used to distinguish between public and authorized calls.
 */
fun <D : Operation.Data> ApolloCall<D>.asAuthorizedCall(): ApolloCall<D> = apply {
    addHttpHeader(AUTHORIZED_CALL_HEADER_NAME, "true")
}

/**
 * Checks if the call is a public call by checking for the presence of a [AUTHORIZED_CALL_HEADER_NAME] header.
 */
fun Request.isPublicCall(): Boolean = headers[AUTHORIZED_CALL_HEADER_NAME] == null

/**
 * Removes the [AUTHORIZED_CALL_HEADER_NAME] header from the request builder.
 */
fun Request.Builder.removeAuthorizedCall() = removeHeader(AUTHORIZED_CALL_HEADER_NAME)

fun Request.Builder.replaceBearerToken(accessToken: String): Request.Builder {
    removeHeader(AUTHORIZATION_HEADER_NAME)
    addBearerToken(accessToken)
    return this
}

fun Request.Builder.addBearerToken(accessToken: String): Request.Builder {
    addHeader(AUTHORIZATION_HEADER_NAME, "$AUTHORIZATION_TYPE_BEARER $accessToken")
    return this
}

fun Request.getBearerToken(): String? {
    val header = headers[AUTHORIZATION_HEADER_NAME] ?: return null
    if (!header.startsWith(AUTHORIZATION_TYPE_BEARER)) return null
    return header.substring(startIndex = AUTHORIZATION_TYPE_BEARER.length + 1)
}