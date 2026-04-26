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

package ru.vladsaybulin.model.character

import kotlinx.datetime.Instant
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.annotatedtext.SeanimeText
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.person.Person

data class CharacterDetails(
    val id: Long,
    val name: String,
    val nameRu: String?,
    val poster: Image?,
    val alternativeName: String?,
    val nameJp: String?,
    val description: SeanimeText?,
    val descriptionSource: String?,
    val topicId: Long?,
    val updatedAt: Instant,
    val seyu: List<Person>,
    val animes: List<Anime>,
    val mangas: List<Manga>
)