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
import ru.vladsaybulin.model.anime.Video
import ru.vladsaybulin.model.character.Character
import ru.vladsaybulin.model.character.CharacterWithRole
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.person.PersonWithRoles
import ru.vladsaybulin.model.related.RelatedTitle
import ru.vladsaybulin.model.search.QueryMapKey
import ru.vladsaybulin.model.title.Title
import ru.vladsaybulin.model.title.TitleDetails
import ru.vladsaybulin.model.title.TitleRoles

interface AnimeRepository {

    /**
     * Returns a stream of brief information about an anime.
     */
    fun animeBriefStream(animeId: Long): Flow<Title>

    /**
     * Returns a stream of detailed information about an anime.
     * This method should trigger update all data (brief, details, roles, similar)
     * @param forceRefresh whether to force a refresh of the data
     */
    fun animeDetailsStream(animeId: Long, forceRefresh: Boolean): Flow<TitleDetails>

    /**
     * Returns a stream of roles associated with an anime.
     */
    fun animeRolesStream(animeId: Long): Flow<TitleRoles>

    /**
     * Returns a stream of similar animes to the specified anime.
     */
    fun animeSimilarStream(animeId: Long): Flow<List<Title>>

    fun animeSearchPagingSource(queryMap: Map<QueryMapKey, String>): PagingSource<Int, Anime>

    fun getOngoingAnimesStream(limit: Int): Flow<List<Anime>>

    fun getAnimePosterStream(animeId: Long): Flow<Image?>

    suspend fun refreshOngoingAnimes(limit: Int, force: Boolean)
}

