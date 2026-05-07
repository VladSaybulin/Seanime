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
import ru.vladsaybulin.model.character.Character
import ru.vladsaybulin.model.character.CharacterWithRole
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.manga.MangaDetails
import ru.vladsaybulin.model.person.PersonWithRoles
import ru.vladsaybulin.model.related.RelatedTitle
import ru.vladsaybulin.model.search.QueryMapKey

interface MangaRepository {
    fun mangaSearchPagingSource(queryMap: Map<QueryMapKey, String>): PagingSource<Int, Manga>

    fun getMangaDetailsStream(mangaId: Long): Flow<MangaDetails>

    fun getFirstMangaRelatedStream(mangaId: Long, limit: Int): Flow<List<RelatedTitle>>

    fun getMangaMainCharactersStream(mangaId: Long): Flow<List<Character>>

    fun getMangaMainAuthorsStream(mangaId: Long): Flow<List<PersonWithRoles>>

    fun getSimilarMangasStream(mangaId: Long): Flow<List<Manga>>

    fun getAllMangaAuthors(mangaId: Long): Flow<List<PersonWithRoles>>

    suspend fun refreshMangaDetails(mangaId: Long)

    suspend fun refreshMangaRoles(mangaId: Long)

    suspend fun refreshSimilarMangas(mangaId: Long)

    fun getAllMangaRelatedTitles(mangaId: Long): Flow<List<RelatedTitle>>

    fun getAllMangaCharacters(mangaId: Long): Flow<List<CharacterWithRole>>
}

