package ru.vladsaybulin.feature.details.model

import ru.vladsaybulin.model.AnimeDetails
import ru.vladsaybulin.model.AnimeKind
import ru.vladsaybulin.model.AnimeRating
import ru.vladsaybulin.model.CharacterWithRole
import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.Genre
import ru.vladsaybulin.model.IncompleteDate
import ru.vladsaybulin.model.PersonWithRoles
import ru.vladsaybulin.model.Poster
import ru.vladsaybulin.model.RelatedEntry
import ru.vladsaybulin.model.Screenshot
import ru.vladsaybulin.model.Statistic
import ru.vladsaybulin.model.Studio
import ru.vladsaybulin.model.UserRateStatus
import ru.vladsaybulin.model.Video

data class EntryDetails internal constructor(
    val id: Long,
    val entryType: EntryType,
    val originalName: String,
    val russianName: String?,
    val englishName: String?,
    val japaneseName: String?,
    val alternativeName: List<String>,
    val licenseNameRu: String?,
    val poster: Poster?,
    val animeKind: AnimeKind?,
    val score: Float?,
    val status: EntryStatus,
    val rating: AnimeRating,
    val episodes: Int,
    val episodesAired: Int,
    val duration: Int?,
    val airedOn: IncompleteDate?,
    val releasedOn: IncompleteDate?,
    val descriptionBBCode: String?,
    val descriptionSource: String?,
    val genres: List<Genre>?,
    val subbers: List<String>?,
    val dubbers: List<String>?,
    val scoreStats: List<Statistic<Int>>?,
    val userRateStatusStats: List<Statistic<UserRateStatus>>?,
    val studios: List<Studio>?,
    val authors: List<PersonWithRoles>?,
    val characters: List<CharacterWithRole>?,
    val relations: List<RelatedEntry>?,
    val screenshots: List<Screenshot>,
    val videos: List<Video>?
)

fun AnimeDetails.asDetails() = EntryDetails(
    id = id,
    entryType = EntryType.Anime,
    originalName = originalName,
    russianName = russianName,
    englishName = englishName,
    japaneseName = japaneseName,
    alternativeName = alternativeName,
    licenseNameRu = licenseNameRu,
    poster = poster,
    animeKind = kind,
    score = score,
    status = status,
    rating = rating,
    episodes = episodes,
    episodesAired = episodesAired,
    duration = duration,
    airedOn = airedOn,
    releasedOn = releasedOn,
    descriptionBBCode = descriptionBBCode,
    descriptionSource = descriptionSource,
    genres = genres,
    subbers = subbers,
    dubbers = dubbers,
    scoreStats = scoreStats,
    userRateStatusStats = userRateStatusStats,
    studios = studios,
    authors = authors,
    characters = characters,
    relations = relations,
    screenshots = screenshots,
    videos = videos
)

val EntryDetails.shouldShowEntryStatus
    get() = status == EntryStatus.None

val EntryDetails.shouldShowDates
    get() = airedOn == null && releasedOn == null

val EntryDetails.shouldShowAnimeKind
    get() = animeKind != AnimeKind.None

val EntryDetails.shouldShowEpisodes
    get() = episodes > 0 || episodesAired > 0

