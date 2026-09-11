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

package ru.vladsaybulin.core.domain.shared

import kotlinx.coroutines.flow.StateFlow
import ru.vladsaybulin.core.domain.repository.AuthRepository
import ru.vladsaybulin.model.auth.SessionState
import javax.inject.Inject

/** Returns the reactive [SessionState] stream from [AuthRepository]. */
class GetAuthStateStreamUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): StateFlow<SessionState> = authRepository.sessionState
}