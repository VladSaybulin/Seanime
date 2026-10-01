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

import ru.vladsaybulin.core.domain.repository.UserRateRepository
import ru.vladsaybulin.model.common.EntryType
import javax.inject.Inject

class GetUserRateByTitleStreamUseCase @Inject constructor(private val repository: UserRateRepository) {
    operator fun invoke(titleType: EntryType, titleId: Long) = when (titleType) {
        EntryType.Anime -> repository.getAnimeUserRateStream(titleId)
        EntryType.Manga -> repository.getMangaUserRateStream(titleId)
    }
}