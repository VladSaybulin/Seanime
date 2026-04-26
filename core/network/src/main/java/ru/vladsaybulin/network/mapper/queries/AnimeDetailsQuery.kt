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

package ru.vladsaybulin.network.mapper.queries

import ru.vladsaybulin.core.network.graphql.AnimeDetailsQuery
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.search.SeasonOfYear
import ru.vladsaybulin.model.search.TimePeriodAiring
import ru.vladsaybulin.network.mapper.enums.asAnimeKind
import ru.vladsaybulin.network.mapper.enums.asAnimeRating
import ru.vladsaybulin.network.mapper.enums.asEntryStatus
import ru.vladsaybulin.network.mapper.enums.asGenreKind
import ru.vladsaybulin.network.mapper.enums.asRelationType
import ru.vladsaybulin.network.mapper.enums.asUserRateStatus
import ru.vladsaybulin.network.mapper.enums.asVideoKind
import ru.vladsaybulin.network.mapper.fragments.asNetworkModel
import ru.vladsaybulin.network.models.common.NetworkGenre
import ru.vladsaybulin.network.models.anime.NetworkStudio
import ru.vladsaybulin.network.models.anime.NetworkAnimeDetails
import ru.vladsaybulin.network.models.anime.NetworkVideo
import ru.vladsaybulin.network.models.common.NetworkImage
import ru.vladsaybulin.network.models.common.NetworkStatisticsItem
import ru.vladsaybulin.network.models.related.NetworkRelated

internal fun AnimeDetailsQuery.Anime.asNetworkModel() = NetworkAnimeDetails(
    id = id,
    name = name,
    nameRu = russian,
    nameEn = english,
    nameJp = japanese,
    alternativeName = synonyms,
    licenseNameRu = licenseNameRu,
    poster = poster?.posterFragment?.asNetworkModel(),
    kind = kind.asAnimeKind(),
    score = score?.toFloat() ?: 0f,
    status = status.asEntryStatus(),
    rating = rating.asAnimeRating(),
    episodes = episodes,
    episodesAired = episodesAired,
    nextEpisodeAt = nextEpisodeAt,
    duration = duration,
    airedOn = airedOn?.incompleteDateFragment?.asNetworkModel(),
    releasedOn = releasedOn?.incompleteDateFragment?.asNetworkModel(),
    season = season?.parseSeasonOrNull(),
    descriptionHtml = descriptionHtml?.takeIf { it.isNotBlank() },
    descriptionSource = descriptionSource?.takeIf { it.isNotBlank() },
    genres = genres?.map(AnimeDetailsQuery.Genre::asNetworkModel),
    subbers = fansubbers,
    dubbers = fandubbers,
    scoreStats = scoresStats?.map(AnimeDetailsQuery.ScoresStat::asNetworkModel),
    userRateStatusStats = statusesStats?.map(AnimeDetailsQuery.StatusesStat::asNetworkModel),
    studios = studios.map(AnimeDetailsQuery.Studio::asNetworkModel),
    related = related?.mapNotNull(AnimeDetailsQuery.Related::asNetworkModel),
    screenshots = screenshots.map(AnimeDetailsQuery.Screenshot::asNetworkModel),
    videos = videos.map(AnimeDetailsQuery.Video::asNetworkModel)
)

private fun AnimeDetailsQuery.Genre.asNetworkModel() = NetworkGenre(
    id = id,
    name = name,
    russianName = russian,
    entryType = EntryType.Anime,
    kind = kind.asGenreKind()
)

private fun AnimeDetailsQuery.ScoresStat.asNetworkModel() =
    NetworkStatisticsItem(score, count)

private fun AnimeDetailsQuery.StatusesStat.asNetworkModel() =
    NetworkStatisticsItem(status.asUserRateStatus(), count)

private fun AnimeDetailsQuery.Studio.asNetworkModel() = NetworkStudio(
    id = id,
    name = name,
    image = imageUrl
)

private fun AnimeDetailsQuery.Related.asNetworkModel() = if (anime != null || manga != null) {
    NetworkRelated(
        anime = anime?.animeFragment?.asNetworkModel(),
        manga = manga?.mangaFragment?.asNetworkModel(),
        relationType = relationKind.asRelationType()
    )
} else null

private fun AnimeDetailsQuery.Screenshot.asNetworkModel() = NetworkImage(
    previewUrl = x332Url,
    originalUrl = originalUrl
)

private fun AnimeDetailsQuery.Video.asNetworkModel() = NetworkVideo(
    name = name,
    previewImageUrl = "https:$imageUrl",
    videoUrl = url,
    playerUrl = "https:$imageUrl",
    kind = kind.asVideoKind()
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