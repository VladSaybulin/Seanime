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

package ru.vladsaybulin.core.domain.titledetails

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import ru.vladsaybulin.core.auth.ShikimoriAuthorization
import ru.vladsaybulin.data.repository.UserRateRepository
import ru.vladsaybulin.model.auth.ShikimoriAuthState
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRate
import javax.inject.Inject

class GetUserRateScreamUseCase @Inject constructor(
    private val auth: ShikimoriAuthorization,
    private val userRateRepository: UserRateRepository
) {
    operator fun invoke(titleType: EntryType, titleId: Long): Flow<UserRateResult> =
        auth.shikimoriAuthState.flatMapLatest {
            if (it == ShikimoriAuthState.LOGGED_IN) {
                when (titleType) {
                    EntryType.Anime -> userRateRepository.getAnimeUserRateStream(titleId)
                    EntryType.Manga -> userRateRepository.getMangaUserRateStream(titleId)
                }.map(UserRateResult::Success)
            } else flowOf(UserRateResult.NotAuthorized)
        }
}

sealed class UserRateResult {
    data object NotAuthorized : UserRateResult()
    data class Success(val userRate: UserRate?) : UserRateResult()
}