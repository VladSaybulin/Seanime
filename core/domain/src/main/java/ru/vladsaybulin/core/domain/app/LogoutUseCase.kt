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

package ru.vladsaybulin.core.domain.app

import ru.vladsaybulin.core.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Triggers the full logout sequence via [ru.vladsaybulin.core.domain.repository.AuthRepository]:
 *  1. Clears encrypted token store
 *  2. Resets SessionState → LoggedOut
 *  3. Runs local DB cleanup (UserRates)
 *  4. Best-effort backend revoke
 */
class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.logout()
}