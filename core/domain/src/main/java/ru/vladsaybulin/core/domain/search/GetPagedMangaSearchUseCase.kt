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
import ru.vladsaybulin.core.domain.repository.MangaRepository
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.manga.MangaKind
import ru.vladsaybulin.model.manga.mangaKind
import ru.vladsaybulin.model.manga.ranobeKind
import ru.vladsaybulin.model.search.QueryMapKey
import javax.inject.Inject

class GetPagedMangaSearchUseCase @Inject constructor(
    private val mangaRepository: MangaRepository,
    private val shikimoriAuthorization: ShikimoriAuthorization
) {
    operator fun invoke(
        queryMap: Map<QueryMapKey, String>,
        isRanobe: Boolean,
        pagingConfig: PagingConfig = DefaultSearchPagingConfig
    ): Flow<PagingData<Manga>> {
        val finalQueryMap = queryMap.let { original ->
            if (!original.containsKey(QueryMapKey.Kind))  {
                val kindValue = (if (isRanobe) ranobeKind else mangaKind)
                    .joinToString(separator = ",", transform = MangaKind::serializedName)
                original + (QueryMapKey.Kind to kindValue)
            } else original
        }
        return pagedSearch(authStateFlow = shikimoriAuthorization.shikimoriAuthState, config = pagingConfig) {
            mangaRepository.mangaSearchPagingSource(finalQueryMap)
        }
    }
}