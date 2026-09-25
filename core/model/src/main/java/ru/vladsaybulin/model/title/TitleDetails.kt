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

package ru.vladsaybulin.model.title

import kotlinx.datetime.Instant
import ru.vladsaybulin.model.anime.AnimeRating
import ru.vladsaybulin.model.anime.Studio
import ru.vladsaybulin.model.anime.Video
import ru.vladsaybulin.model.annotatedtext.SeanimeText
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.common.StatisticsItem
import ru.vladsaybulin.model.genre.Genre
import ru.vladsaybulin.model.manga.Publisher
import ru.vladsaybulin.model.search.TimePeriodAiring
import ru.vladsaybulin.model.userrate.UserRateStatus

/**
 * A detailed representation of a title (anime or manga).
 */
data class TitleDetails(

    // Shared fields
    val englishName: String?,
    val japaneseName: String?,
    val alternativeNames: String?,
    val licensedName: String?,
    val description: SeanimeText?,
    val descriptionSource: String?,
    val scoreStats: List<StatisticsItem<Int>>?,
    val userRateStatusStats: List<StatisticsItem<UserRateStatus>>?,
    val genres: List<Genre>,

    // Anime specific
    val nextEpisodeAt: Instant?,
    val rating: AnimeRating,
    val season: TimePeriodAiring.Season?,
    val studios: List<Studio>,
    val subbers: List<String>?,
    val dubbers: List<String>?,
    val screenshots: List<Image>?,
    val videos: List<Video>?,

    // Manga specific
    val publishers: List<Publisher>,
)