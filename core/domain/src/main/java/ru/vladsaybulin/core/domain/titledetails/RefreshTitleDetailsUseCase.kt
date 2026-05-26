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
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import ru.vladsaybulin.core.domain.repository.AnimeRepository
import ru.vladsaybulin.core.domain.repository.MangaRepository
import ru.vladsaybulin.model.common.EntryType
import javax.inject.Inject

class RefreshTitleDetailsUseCase @Inject constructor(
    private val animeRepository: Lazy<AnimeRepository>,
    private val mangaRepository: Lazy<MangaRepository>,
) {

    enum class RefreshCompleted {
        Details,
        Roles,
        Similar,
        SkipRefresh
    }

    operator fun invoke(titleType: EntryType, titleId: Long, forceRefresh: Boolean): Flow<RefreshCompleted> =
        channelFlow {
            when (titleType) {
                EntryType.Anime -> refreshAnime(titleId, forceRefresh)
                EntryType.Manga -> refreshManga(titleId, forceRefresh)
            }
        }

    private suspend fun SendChannel<RefreshCompleted>.refreshAnime(animeId: Long, forceRefresh: Boolean) =
        animeRepository.get().apply {
            coroutineScope {
                launch {
                    refreshAnimeDetails(animeId, forceRefresh)
                    send(RefreshCompleted.Details)
                }

                launch {
                    refreshAnimeRoles(animeId, forceRefresh)
                    send(RefreshCompleted.Roles)
                }

                launch {
                    refreshSimilarAnimes(animeId, forceRefresh)
                    send(RefreshCompleted.Similar)
                }
            }
        }

    private suspend fun SendChannel<RefreshCompleted>.refreshManga(mangaId: Long, forceRefresh: Boolean) = mangaRepository.get().apply {
        coroutineScope {
            launch {
                refreshMangaDetails(mangaId, forceRefresh)
                send(RefreshCompleted.Details)
            }

            launch {
                refreshMangaRoles(mangaId, forceRefresh)
                send(RefreshCompleted.Roles)
            }

            launch {
                refreshSimilarMangas(mangaId, forceRefresh)
                send(RefreshCompleted.Similar)
            }
        }
    }
}