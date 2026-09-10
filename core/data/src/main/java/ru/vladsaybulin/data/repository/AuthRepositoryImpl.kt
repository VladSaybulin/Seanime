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

package ru.vladsaybulin.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.vladsaybulin.core.auth.SessionManager
import ru.vladsaybulin.core.domain.repository.AuthRepository
import ru.vladsaybulin.model.auth.SessionState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val sessionManager: SessionManager
) : AuthRepository {

    override val sessionState: StateFlow<SessionState>
        get() = sessionManager.sessionState

    override fun getUserIdStream(): Flow<Long?> = sessionManager.userIdStream()

    override suspend fun logout() {
        sessionManager.logout()
    }
}

