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

package ru.vladsaybulin.core.auth

/**
 * Single backend endpoint (action=exchange|refresh).
 * See docs/auth_backend_contract.md.
 *
 * Throws [java.io.IOException] on network errors,
 * [ru.vladsaybulin.core.auth.AuthException.Unauthorized] when the backend rejects tokens.
 */
interface TokenExchangeGateway {
    suspend fun exchange(code: String): StoredTokens
    suspend fun refresh(refreshToken: String): StoredTokens
}

/** Fatal auth error from the backend (e.g. 401 on refresh). */
sealed class AuthException(message: String) : Exception(message) {
    class Unauthorized : AuthException("Session is no longer valid")
}
