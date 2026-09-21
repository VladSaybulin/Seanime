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

package ru.vladsaybulin.feature.rate.editor.api.navigation

import kotlinx.serialization.Serializable
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.manga.Manga

/**
 * Represents a reference to a title (anime or manga) with its type and ID.
 * @property titleType The type of the title (anime or manga).
 * @property titleId The ID of the title.
 */
@Serializable
data class TitleReference(
    val titleType: EntryType,
    val titleId: Long
) {
}

fun Anime.titleReference() = TitleReference(EntryType.Anime, id)

fun Manga.titleReference() = TitleReference(EntryType.Manga, id)