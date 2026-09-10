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

package ru.vladsaybulin.core.domain.profile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import ru.vladsaybulin.core.domain.repository.AuthRepository
import ru.vladsaybulin.core.domain.repository.UserRepository
import ru.vladsaybulin.model.user.BriefUser
import javax.inject.Inject

class GetBriefUserStreamUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {
    operator fun invoke(userId: Long?): Flow<BriefUser?> =
        if (userId == null) {
            // Current user: derive from authenticated session
            authRepository.getUserIdStream().flatMapLatest { myId ->
                if (myId == null) flowOf(null)
                else userRepository.getUserStream(myId).map<BriefUser, BriefUser?> { it }
            }
        } else {
            userRepository.getUserStream(userId).map<BriefUser, BriefUser?> { it }
        }
}