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

package ru.vladsaybulin.core.domain.search

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.core.auth.ShikimoriAuthorization
import ru.vladsaybulin.data.repository.AnimeRepository
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.search.QueryMapKey
import javax.inject.Inject

class GetPagedAnimeSearchUseCase @Inject internal constructor(
    private val animeRepository: AnimeRepository,
    private val shikimoriAuthorization: ShikimoriAuthorization
) {
    operator fun invoke(
        queryMap: Map<QueryMapKey, String>,
        pagingConfig: PagingConfig = DefaultSearchPagingConfig
    ): Flow<PagingData<Anime>> = pagedSearch(
        authStateFlow = shikimoriAuthorization.shikimoriAuthState,
        config = pagingConfig
    ) {
        animeRepository.animeSearchPagingSource(queryMap)
    }
}

