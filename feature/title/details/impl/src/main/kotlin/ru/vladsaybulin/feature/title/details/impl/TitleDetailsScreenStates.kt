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

package ru.vladsaybulin.feature.title.details.impl

import kotlinx.datetime.Instant
import ru.vladsaybulin.model.anime.AnimeRating
import ru.vladsaybulin.model.anime.Studio
import ru.vladsaybulin.model.anime.Video
import ru.vladsaybulin.model.annotatedtext.SeanimeText
import ru.vladsaybulin.model.character.CharacterWithRole
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.common.IncompleteDate
import ru.vladsaybulin.model.common.StatisticsItem
import ru.vladsaybulin.model.genre.Genre
import ru.vladsaybulin.model.manga.Publisher
import ru.vladsaybulin.model.person.PersonWithRoles
import ru.vladsaybulin.model.related.RelatedTitle
import ru.vladsaybulin.model.search.SearchType
import ru.vladsaybulin.model.search.TimePeriodAiring
import ru.vladsaybulin.model.title.RanobeKindList
import ru.vladsaybulin.model.title.Title
import ru.vladsaybulin.model.title.TitleKind
import ru.vladsaybulin.model.userrate.UserRate
import ru.vladsaybulin.model.userrate.UserRateContext
import ru.vladsaybulin.model.userrate.UserRateStatus

sealed interface TitleDetailsLoadState<T> {
    class Loading <T> : TitleDetailsLoadState<T>
    data class Success<T>(val data: T) : TitleDetailsLoadState<T>
}

data class HeaderData(
    val poster: Image?,
    val name: String,
    val nameRu: String?
)

data class InfoData(
    val titleType: EntryType,
    val kind: TitleKind,
    val status: EntryStatus,
    val score: Float,
    val episodes: Int,
    val episodesAired: Int,
    val episodeDuration: Int,
    val chapters: Int,
    val volumes: Int,
    val airedOn: IncompleteDate?,
    val releasedOn: IncompleteDate?,
    val season: TimePeriodAiring.Season?,
    val rating: AnimeRating,
    val nextEpisodeAt: Instant?,
    val studios: List<Studio>,
    val publishers: List<Publisher>,
    val genres: List<Genre>,
    val description: SeanimeText?,
    val descriptionSource: String?,
    val scoreStats: List<StatisticsItem<Int>>,
    val statusStats: List<StatisticsItem<UserRateStatus>>
)

data class AnimeMediaData(
    val screenshots: List<Image>,
    val videos: List<Video>
)

typealias HeaderState = TitleDetailsLoadState<HeaderData>
typealias InfoState = TitleDetailsLoadState<InfoData>
typealias AuthorsState = TitleDetailsLoadState<ExpandableSectionState<PersonWithRoles>>
typealias CharactersState = TitleDetailsLoadState<ExpandableSectionState<CharacterWithRole>>
typealias RelatedTitlesState = TitleDetailsLoadState<ExpandableSectionState<RelatedTitle>>
typealias SimilarTitlesState = TitleDetailsLoadState<List<Title>>
typealias AnimeMediaState = TitleDetailsLoadState<AnimeMediaData>
typealias UserRateState = TitleDetailsLoadState<UserRate?>

typealias ErrorState = Throwable

fun HeaderData.asSuccess() = TitleDetailsLoadState.Success(this)
fun InfoData.asSuccess() = TitleDetailsLoadState.Success(this)
inline fun <reified T> ExpandableSectionState<T>.asSuccess() = TitleDetailsLoadState.Success(this)
fun List<Title>.asSuccess() = TitleDetailsLoadState.Success(this)
fun AnimeMediaData.asSuccess() = TitleDetailsLoadState.Success(this)
fun UserRate?.asSuccess() = TitleDetailsLoadState.Success(this)

fun InfoData.searchType() = when {
    titleType == EntryType.Anime -> SearchType.Anime
    kind in RanobeKindList -> SearchType.Ranobe
    else -> SearchType.Manga
}