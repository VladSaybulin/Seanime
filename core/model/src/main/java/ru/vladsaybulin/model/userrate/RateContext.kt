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

package ru.vladsaybulin.model.userrate

import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.manga.Manga

class UserRateContext(
    val titleStatus: EntryStatus,
    val maxEpisodes: Int,
    val maxChapters: Int,
    val maxVolumes: Int
)

fun Anime.extractRateContext(): UserRateContext {
    return UserRateContext(
        titleStatus = status,
        maxEpisodes = episodes,
        maxChapters = 0,
        maxVolumes = 0
    )
}

fun Manga.extractRateContext(): UserRateContext {
    return UserRateContext(
        titleStatus = status,
        maxEpisodes = 0,
        maxChapters = chapters,
        maxVolumes = volumes
    )
}