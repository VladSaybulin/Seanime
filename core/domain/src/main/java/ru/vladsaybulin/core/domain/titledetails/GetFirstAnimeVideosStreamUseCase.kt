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
import kotlinx.coroutines.flow.map
import ru.vladsaybulin.core.domain.repository.AnimeRepository
import ru.vladsaybulin.model.anime.Video
import ru.vladsaybulin.model.common.DataSlice
import javax.inject.Inject

class GetFirstAnimeVideosStreamUseCase @Inject constructor(
    private val animeRepository: AnimeRepository
) {
    operator fun invoke(animeId: Long, limit: Int = DefaultAnimeVideosLimit): Flow<DataSlice<Video>> =
        animeRepository.getFirstAnimeVideos(animeId, limit + 1)
            .map { videos ->
                val hasMore = videos.size == limit + 1
                DataSlice(
                    data = if (hasMore) videos.slice(0..<limit) else videos,
                    hasMore = hasMore
                )
            }
}

private const val DefaultAnimeVideosLimit = 5