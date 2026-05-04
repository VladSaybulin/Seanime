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

/**
 * Sets `Authorization: Bearer <token>` header for this request builder.
 *
 * @param token Access token to include into Authorization header.
 * @return Same [Request.Builder] instance for chaining.
 */
fun Request.Builder.setAuthorizationBearer(token: String): Request.Builder = apply{
    header(AUTHORIZATION_HEADER, "$AUTHORIZATION_TYPE $token")
}

/**
 * Extracts bearer token from Authorization header.
 *
 * @return Token string or `null` when Authorization header is absent.
 * @throws IllegalStateException When Authorization type is not `Bearer`.
 */
fun Request.getAuthorizationBearer(): String? {
    val headerValue = header(AUTHORIZATION_HEADER) ?: return null

    check(headerValue.startsWith(AUTHORIZATION_TYPE)) {
        "Authorization type (${headerValue.substringBefore(' ')} not supported"
    }

    return headerValue.substring(AUTHORIZATION_TYPE.length + 1).trim()
}

private const val AUTHORIZATION_HEADER = "Authorization"
private const val AUTHORIZATION_TYPE = "Bearer"