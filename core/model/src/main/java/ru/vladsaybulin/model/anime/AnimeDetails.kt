package ru.vladsaybulin.model.anime

import kotlinx.datetime.Instant
import ru.vladsaybulin.model.annotatedtext.AnnotatedText
import ru.vladsaybulin.model.character.CharacterWithRole
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.common.IncompleteDate
import ru.vladsaybulin.model.common.StatisticsItem
import ru.vladsaybulin.model.genre.Genre
import ru.vladsaybulin.model.person.PersonWithRoles
import ru.vladsaybulin.model.related.RelatedEntry
import ru.vladsaybulin.model.userrate.UserRateStatus

data class AnimeDetails(
    val id: Long,
    val originalName: String,
    val russianName: String?,
    val englishName: String?,
    val japaneseName: String?,
    val alternativeName: List<String>,
    val licenseNameRu: String?,
    val poster: Image?,
    val kind: AnimeKind,
    val score: Float,
    val status: EntryStatus,
    val rating: AnimeRating,
    val episodes: Int,
    val episodesAired: Int,
    val duration: Int?,
    val nextEpisodeAt: Instant?,
    val airedOn: IncompleteDate?,
    val releasedOn: IncompleteDate?,
    val description: AnnotatedText?,
    val descriptionSource: String?,
    val genres: List<Genre>?,
    val subbers: List<String>?,
    val dubbers: List<String>?,
    val scoreStats: List<StatisticsItem<Int>>?,
    val userRateStatusStats: List<StatisticsItem<UserRateStatus>>?,
    val studios: List<Studio>?,
    val authors: List<PersonWithRoles>?,
    val characters: List<CharacterWithRole>?,
    val related: List<RelatedEntry>?,
    val screenshots: List<Image>,
    val videos: List<Video>?
)