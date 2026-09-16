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

package ru.vladsaybulin.feature.list.title.details.navigation

import kotlinx.serialization.Serializable
import ru.vladsaybulin.core.navigation.Navigator
import ru.vladsaybulin.core.navigation.SeanimeNavKey
import ru.vladsaybulin.model.common.EntryType

/**
 * Navigation key for the title details feature.
 * @param titleType The type of the title (Anime or Manga).
 * @param titleId The ID of the title.
 */
@Serializable
data class TitleDetailsNavKey(val titleType: EntryType, val titleId: Long) : SeanimeNavKey

/**
 * Navigates to the title details screen.
 * @param titleType The type of the title (Anime or Manga).
 * @param titleId The ID of the title.
 */
fun Navigator.navigateToTitle(titleType: EntryType, titleId: Long) {
    navigateTo(TitleDetailsNavKey(titleType, titleId))
}

/**
 * Navigates to the anime details screen.
 * @param animeId The ID of the anime.
 */
fun Navigator.navigateToAnime(animeId: Long) {
    navigateTo(TitleDetailsNavKey(EntryType.Anime, animeId))
}

/**
 * Navigates to the manga details screen.
 * @param mangaId The ID of the manga.
 */
fun Navigator.navigateToManga(mangaId: Long) {
    navigateTo(TitleDetailsNavKey(EntryType.Manga, mangaId))
}