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

package ru.vladsaybulin.core.domain.repository

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.AnimeDetails
import ru.vladsaybulin.model.anime.Video
import ru.vladsaybulin.model.character.Character
import ru.vladsaybulin.model.character.CharacterWithRole
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.person.PersonWithRoles
import ru.vladsaybulin.model.related.RelatedTitle
import ru.vladsaybulin.model.search.QueryMapKey

interface AnimeRepository {
    fun animeSearchPagingSource(queryMap: Map<QueryMapKey, String>): PagingSource<Int, Anime>

    fun getOngoingAnimesStream(limit: Int): Flow<List<Anime>>

    fun getAnimeDetailsStream(animeId: Long): Flow<AnimeDetails>

    fun getAnimeMainCharactersStream(animeId: Long): Flow<List<Character>>

    fun getAnimeMainAuthorsStream(animeId: Long): Flow<List<PersonWithRoles>>

    fun getFirstAnimeRelatedStream(animeId: Long, limit: Int): Flow<List<RelatedTitle>>

    fun getAnimeScreenshots(animeId: Long): Flow<List<Image>>

    fun getFirstAnimeVideos(animeId: Long, limit: Int): Flow<List<Video>>

    fun getSimilarAnimes(animeId: Long): Flow<List<Anime>>

    fun getAllAnimeAuthors(animeId: Long): Flow<List<PersonWithRoles>>

    fun getAllAnimeRelatedTitles(animeId: Long): Flow<List<RelatedTitle>>

    fun getAllAnimeCharacters(animeId: Long): Flow<List<CharacterWithRole>>

    fun getAllAnimeVideos(animeId: Long): Flow<List<Video>>

    suspend fun refreshAnimeDetails(animeId: Long, force: Boolean)

    suspend fun refreshAnimeRoles(animeId: Long, force: Boolean)

    suspend fun refreshSimilarAnimes(animeId: Long, force: Boolean)

    suspend fun refreshOngoingAnimes(limit: Int, force: Boolean)
}

