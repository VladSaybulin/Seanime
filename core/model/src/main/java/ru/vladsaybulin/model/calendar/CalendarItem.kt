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

package ru.vladsaybulin.model.calendar

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.previewAnimes
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

data class CalendarItem(
    val anime: Anime,
    val nextEpisode: Int,
    val nextEpisodeAt: Instant,
    val duration: Duration?
)

val previewCalendarItems = listOf(
    CalendarItem(
        anime = previewAnimes[0],
        nextEpisode = 2,
        nextEpisodeAt = Clock.System.now(),
        duration = 30.minutes
    ),
    CalendarItem(
        anime = previewAnimes[1],
        nextEpisode = 0,
        nextEpisodeAt = Clock.System.now(),
        duration = null
    ),
)