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

import okhttp3.Request

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

private const val AUTHORIZATION_HEADER_NAME = "Authorization"
private const val AUTHORIZATION_TYPE_BEARER = "Bearer"