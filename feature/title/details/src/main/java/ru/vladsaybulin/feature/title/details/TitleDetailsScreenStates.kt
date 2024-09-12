package ru.vladsaybulin.feature.title.details

import kotlinx.datetime.Instant
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.AnimeDetails
import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.anime.AnimeRating
import ru.vladsaybulin.model.anime.Studio
import ru.vladsaybulin.model.anime.Video
import ru.vladsaybulin.model.annotatedtext.SeanimeText
import ru.vladsaybulin.model.character.Character
import ru.vladsaybulin.model.common.DataSlice
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.common.IncompleteDate
import ru.vladsaybulin.model.common.StatisticsItem
import ru.vladsaybulin.model.genre.Genre
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.manga.MangaDetails
import ru.vladsaybulin.model.manga.MangaKind
import ru.vladsaybulin.model.manga.Publisher
import ru.vladsaybulin.model.manga.ranobeKind
import ru.vladsaybulin.model.person.PersonWithRoles
import ru.vladsaybulin.model.related.RelatedEntry
import ru.vladsaybulin.model.search.SearchType
import ru.vladsaybulin.model.search.TimePeriodAiring
import ru.vladsaybulin.model.userrate.EditableUserRate
import ru.vladsaybulin.model.userrate.UserRate
import ru.vladsaybulin.model.userrate.UserRateStatus

sealed class TitleDetailsState {
    data object Loading : TitleDetailsState()

    data class Success(
        val entryType: EntryType,
        val entryId: Long,
        val poster: Image?,
        val name: String,
        val russianName: String?,
        val status: EntryStatus,
        val animeKind: AnimeKind,
        val mangaKind: MangaKind,
        val score: Float,
        val episodes: Int,
        val episodesAired: Int,
        val episodeDuration: Int,
        val chapters: Int,
        val volumes: Int,
        val nextEpisodeAt: Instant?,
        val airedOn: IncompleteDate?,
        val releasedOn: IncompleteDate?,
        val season: TimePeriodAiring.Season?,
        val rating: AnimeRating,
        val studios: List<Studio>,
        val publishers: List<Publisher>,
        val genres: List<Genre>,
        val description: SeanimeText?,
        val descriptionSource: String?,
        val scoreStatisticsItems: List<StatisticsItem<Int>>,
        val userRateStatusStatisticItems: List<StatisticsItem<UserRateStatus>>?,
        val relatedSlice: DataSlice<RelatedEntry>?,
        val allScreenshots: List<Image>,
        val screenshotsSlice: DataSlice<Image>?,
        val videosSlice: DataSlice<Video>?
    ) : TitleDetailsState()
}

sealed class RolesState {
    data object Loading : RolesState()

    data class Success(
        val mainCharacters: List<Character>,
        val mainAuthors: List<PersonWithRoles>
    ) : RolesState()
}

sealed class SimilarState {
    data object Loading : SimilarState()

    data object Empty : SimilarState()

    data class Animes(val animes: List<Anime>) : SimilarState()

    data class Mangas(val mangas: List<Manga>) : SimilarState()
}

sealed class UserRateState {
    data object Loading : UserRateState()

    data object NotAuthorized : UserRateState()

    data object NoUserRate : UserRateState()

    data class Success(val userRate: UserRate) : UserRateState()
}

internal fun createEditableUserRate(
    detailsState: TitleDetailsState.Success,
    userRateState: UserRateState.Success
) = EditableUserRate(
        userRate = userRateState.userRate,
        titleType = detailsState.entryType,
        entryStatus = detailsState.status,
        maxEpisodes = if (detailsState.entryType == EntryType.Anime) detailsState.episodes else -1,
        maxChapters = if (detailsState.entryType == EntryType.Manga) detailsState.chapters else -1,
        maxVolumes = if (detailsState.entryType == EntryType.Manga) detailsState.volumes else -1
    )

fun successTitleDetails(
    animeDetails: AnimeDetails,
    relatedSlice: DataSlice<RelatedEntry>,
    screenshots: List<Image>,
    videosSlice: DataSlice<Video>
) = with(animeDetails) {
    TitleDetailsState.Success(
        entryType = EntryType.Anime,
        entryId = id,
        poster = poster,
        name = originalName,
        russianName = russianName,
        status = status,
        animeKind = kind,
        score = score,
        episodes = episodes,
        episodesAired = episodesAired,
        episodeDuration = duration,
        nextEpisodeAt = nextEpisodeAt,
        airedOn = airedOn,
        releasedOn = releasedOn,
        season = season,
        rating = rating,
        studios = studios,
        genres = genres,
        description = description,
        descriptionSource = descriptionSource,
        scoreStatisticsItems = scoreStats ?: emptyList(),
        userRateStatusStatisticItems = userRateStatusStats?.filter { it.count > 0 },
        relatedSlice = relatedSlice.nullIfEmpty(),
        allScreenshots = screenshots,
        screenshotsSlice = if (screenshots.isNotEmpty()) {
            screenshots.subList(0, FirstScreenshotsLimit.coerceAtMost(screenshots.size)).let {
                DataSlice(
                    data = it,
                    hasMore = it.size < screenshots.size
                )
            }
        } else null,
        videosSlice = videosSlice.nullIfEmpty(),

        //Manga only fields
        chapters = 0,
        volumes = 0,
        mangaKind = MangaKind.None,
        publishers = emptyList()
    )
}

fun successTitleDetails(
    mangaDetails: MangaDetails,
    relatedSlice: DataSlice<RelatedEntry>
) = with(mangaDetails) {
    TitleDetailsState.Success(
        entryType = EntryType.Manga,
        entryId = id,
        poster = poster,
        name = originalName,
        russianName = russianName,
        status = status,
        mangaKind = kind,
        score = score ?: 0f,
        chapters = chapters,
        volumes = volumes,
        airedOn = airedOn,
        releasedOn = releasedOn,
        publishers = publishers,
        genres = genres,
        description = description,
        descriptionSource = descriptionSource,
        scoreStatisticsItems = scoreStats ?: emptyList(),
        userRateStatusStatisticItems = userRateStatusStats?.filter { it.count > 0 },
        relatedSlice = relatedSlice.nullIfEmpty(),

        //Anime only fields
        episodes = 0,
        episodesAired = 0,
        episodeDuration = 0,
        nextEpisodeAt = null,
        animeKind = AnimeKind.None,
        studios = emptyList(),
        allScreenshots = emptyList(),
        screenshotsSlice = null,
        videosSlice = null,
        rating = AnimeRating.None,
        season = null
    )
}

internal fun TitleDetailsState.Success.searchType() = when (entryType) {
    EntryType.Anime -> SearchType.Anime
    EntryType.Manga -> if (mangaKind !in ranobeKind) SearchType.Manga else SearchType.Ranobe
}

private fun <T> DataSlice<T>.nullIfEmpty(): DataSlice<T>? = takeIf { it.data.isNotEmpty() }

private const val FirstScreenshotsLimit = 5