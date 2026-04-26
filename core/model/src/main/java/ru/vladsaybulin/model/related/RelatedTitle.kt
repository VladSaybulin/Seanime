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

package ru.vladsaybulin.model.related

import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.manga.Manga

sealed interface RelatedTitle {
    val relationType: RelationType
}

class RelatedAnime(val anime: Anime, override val relationType: RelationType) : RelatedTitle

class RelatedManga(val manga: Manga, override val relationType: RelationType) : RelatedTitle