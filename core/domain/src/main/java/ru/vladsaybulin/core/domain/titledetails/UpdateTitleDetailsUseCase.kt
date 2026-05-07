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
import ru.vladsaybulin.core.domain.repository.LastRequestRepository
import ru.vladsaybulin.core.domain.repository.MangaRepository
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.request.Request
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours

class UpdateTitleDetailsUseCase @Inject constructor(
    private val animeRepository: Lazy<AnimeRepository>,
    private val mangaRepository: Lazy<MangaRepository>,
    private val lastRequestRepository: LastRequestRepository
) {

    enum class RefreshCompleted {
        Details,
        Roles,
        Similar,
        SkipRefresh
    }

    operator fun invoke(titleType: EntryType, titleId: Long, forceRefresh: Boolean = false): Flow<RefreshCompleted> = channelFlow {
        when (titleType) {
            EntryType.Anime -> refreshAnime(titleId, forceRefresh)
            EntryType.Manga -> refreshManga(titleId, forceRefresh)
        }
    }

    private suspend fun SendChannel<RefreshCompleted>.refreshAnime(animeId: Long, forceRefresh: Boolean) {
        if (forceRefresh || lastRequestRepository.isRequestExpired(Request.ANIME, animeId, TitleRequestTTL)) {
            val repository = animeRepository.get()
            coroutineScope {
                launch {
                    repository.refreshAnimeDetails(animeId)
                    send(RefreshCompleted.Details)
                }

                launch {
                    repository.refreshAnimeRoles(animeId)
                    send(RefreshCompleted.Roles)
                }

                launch {
                    repository.refreshSimilarAnimes(animeId)
                    send(RefreshCompleted.Similar)
                }

                lastRequestRepository.updateLastRequest(Request.ANIME, animeId)
            }
        } else {
            send(RefreshCompleted.SkipRefresh)
        }
    }

    private suspend fun SendChannel<RefreshCompleted>.refreshManga(mangaId: Long, forceRefresh: Boolean) {
        if (forceRefresh || lastRequestRepository.isRequestExpired(Request.MANGA, mangaId, TitleRequestTTL)) {
            val repository = mangaRepository.get()
            coroutineScope {
                launch {
                    repository.refreshMangaDetails(mangaId)
                    send(RefreshCompleted.Details)
                }

                launch {
                    repository.refreshMangaRoles(mangaId)
                    send(RefreshCompleted.Roles)
                }

                launch {
                    repository.refreshSimilarMangas(mangaId)
                    send(RefreshCompleted.Similar)
                }

                lastRequestRepository.updateLastRequest(Request.MANGA, mangaId)
            }
        } else send(RefreshCompleted.SkipRefresh)
    }

    companion object {
        private val TitleRequestTTL = 24.hours
    }
}