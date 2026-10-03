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

package ru.vladsaybulin.network.mapper.fragments

import ru.vladsaybulin.core.network.graphql.fragment.AnimeDetailsFragment
import ru.vladsaybulin.core.network.graphql.fragment.AnimeFragment
import ru.vladsaybulin.core.network.graphql.fragment.AnimeWithLocalDateFragment
import ru.vladsaybulin.core.network.graphql.fragment.ScreenshotsFragment
import ru.vladsaybulin.core.network.graphql.fragment.StudioFragment
import ru.vladsaybulin.core.network.graphql.fragment.VideoFragment
import ru.vladsaybulin.model.title.TitleStatus
import ru.vladsaybulin.model.search.SeasonOfYear
import ru.vladsaybulin.model.search.TimePeriodAiring
import ru.vladsaybulin.network.mapper.enums.asAnimeKind
import ru.vladsaybulin.network.mapper.enums.asEntryStatus
import ru.vladsaybulin.network.mapper.enums.asVideoKind
import ru.vladsaybulin.network.models.anime.NetworkAnime
import ru.vladsaybulin.network.models.anime.NetworkAnimeDetails
import ru.vladsaybulin.network.models.anime.NetworkStudio
import ru.vladsaybulin.network.models.anime.NetworkVideo
import ru.vladsaybulin.network.models.common.NetworkImage
import ru.vladsaybulin.network.models.userrate.NetworkUserRate

internal fun AnimeFragment.asNetworkModel() = NetworkAnime(
    id = baseAnimeFragment.id,
    originalName = baseAnimeFragment.name,
    russianName = baseAnimeFragment.russian,
    poster = baseAnimeFragment.poster?.posterFragment?.asNetworkModel(),
    kind = baseAnimeFragment.kind.asAnimeKind(),
    status = baseAnimeFragment.status?.asEntryStatus() ?: TitleStatus.None,
    score = baseAnimeFragment.score?.toFloat() ?: 0f,
    episodes = baseAnimeFragment.episodes,
    episodesAired = baseAnimeFragment.episodesAired,
    airedOn = airedOn?.incompleteDateFragment?.asNetworkModel(),
    releasedOn = releasedOn?.incompleteDateFragment?.asNetworkModel()
)

internal fun AnimeWithLocalDateFragment.asNetworkModel() = NetworkAnime(
    id = baseAnimeFragment.id,
    originalName = baseAnimeFragment.name,
    russianName = baseAnimeFragment.russian,
    poster = baseAnimeFragment.poster?.posterFragment?.asNetworkModel(),
    kind = baseAnimeFragment.kind.asAnimeKind(),
    status = baseAnimeFragment.status?.asEntryStatus() ?: TitleStatus.None,
    score = baseAnimeFragment.score?.toFloat() ?: 0f,
    episodes = baseAnimeFragment.episodes,
    episodesAired = baseAnimeFragment.episodesAired,
    airedOn = airedOn?.date?.asIncompleteDate(),
    releasedOn = releasedOn?.date?.asIncompleteDate()
)

internal fun AnimeDetailsFragment.asNetworkModel(userRate: NetworkUserRate?) = NetworkAnimeDetails(
    id = id,
    nameEn = english,
    nameJp = japanese,
    alternativeName = synonyms.joinToString(separator = ", ").takeIf { it.isNotBlank() },
    licenseNameRu = licenseNameRu,
    duration = duration,
    nextEpisodeAt = nextEpisodeAt,
    season = season?.parseSeasonOrNull(),
    descriptionHtml = descriptionHtml?.takeIf { it.isNotBlank() },
    descriptionSource = descriptionSource?.takeIf { it.isNotBlank() },
    genres = genres?.map { it.genreFragment.asNetworkModel() },
    subbers = fansubbers,
    dubbers = fandubbers,
    scoreStats = scoresStats?.map { it.scoreStatFragment.asNetworkModel() },
    userRateStatusStats = statusesStats?.map { it.statusStatFragment.asNetworkModel() },
    studios = studios.map { it.studioFragment.asNetworkModel() },
    related = related?.map { it.relatedEntryFragment.asNetworkModel() },
    screenshots = screenshots.map { it.screenshotsFragment.asNetworkModel() },
    videos = videos.map { it.videoFragment.asNetworkModel() },
    userRate = userRate
)

private fun ScreenshotsFragment.asNetworkModel() = NetworkImage(
    originalUrl = originalUrl,
    previewUrl = x332Url
)

private fun StudioFragment.asNetworkModel() = NetworkStudio(
    id = id,
    name = name,
    image = imageUrl
)

private fun VideoFragment.asNetworkModel() = NetworkVideo(
    name = name,
    previewImageUrl = imageUrl,
    playerUrl = playerUrl,
    kind = kind.asVideoKind(),
    videoUrl = url
)

private fun String.parseSeasonOrNull(): TimePeriodAiring.Season? {
    val delimiterIndex = this.indexOf('-')
    if (delimiterIndex == -1) return null

    val seasonOfYear = when (this.substring(0, delimiterIndex)) {
        "winter" -> SeasonOfYear.Winter
        "spring" -> SeasonOfYear.Spring
        "summer" -> SeasonOfYear.Summer
        "fall" -> SeasonOfYear.Fall
        else -> return null
    }
    val year = this.substring(delimiterIndex, this.length).toIntOrNull() ?: return null

    return TimePeriodAiring.Season(seasonOfYear, year)
}