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

package ru.vladsaybulin.core.domain

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.data.repository.AnimeRepository
import ru.vladsaybulin.data.repository.MangaRepository
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.person.PersonWithRoles
import javax.inject.Inject

class GetAuthorsUseCase @Inject constructor(
    private val animeRepository: Lazy<AnimeRepository>,
    private val mangaRepository: Lazy<MangaRepository>
) {
    operator fun invoke(entryType: EntryType, entryId: Long): Flow<List<PersonWithRoles>> =
        when (entryType) {
            EntryType.Anime -> animeRepository.get().getAllAnimeAuthors(entryId)
            EntryType.Manga -> mangaRepository.get().getAllMangaAuthors(entryId)
        }
}