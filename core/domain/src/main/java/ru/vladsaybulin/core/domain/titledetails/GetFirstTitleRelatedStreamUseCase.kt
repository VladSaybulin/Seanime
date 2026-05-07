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

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.vladsaybulin.core.domain.repository.AnimeRepository
import ru.vladsaybulin.core.domain.repository.MangaRepository
import ru.vladsaybulin.model.common.DataSlice
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.related.RelatedTitle
import javax.inject.Inject

class GetFirstTitleRelatedStreamUseCase @Inject constructor(
    private val animeRepository: Lazy<AnimeRepository>,
    private val mangaRepository: Lazy<MangaRepository>
) {
    operator fun invoke(
        titleType: EntryType,
        titleId: Long,
        limit: Int = DefaultRelatedEntriesLimit
    ): Flow<DataSlice<RelatedTitle>> = when (titleType) {
        EntryType.Anime -> animeRepository.get().getFirstAnimeRelatedStream(titleId, limit + 1)
        EntryType.Manga -> mangaRepository.get().getFirstMangaRelatedStream(titleId, limit + 1)
    }.map { relatedEntries ->
        val hasMore = relatedEntries.size == limit + 1
        DataSlice(
            data = if (hasMore) relatedEntries.slice(0..<limit) else relatedEntries,
            hasMore = hasMore
        )
    }
}

private const val DefaultRelatedEntriesLimit = 3