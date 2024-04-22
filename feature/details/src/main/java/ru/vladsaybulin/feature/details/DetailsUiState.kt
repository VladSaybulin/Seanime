package ru.vladsaybulin.feature.details

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Instant
import ru.vladsaybulin.core.navigation.args.ImageViewArgs
import ru.vladsaybulin.core.navigation.args.SearchArgs
import ru.vladsaybulin.model.EntryDetails
import ru.vladsaybulin.model.SimilarEntry
import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.anime.Studio
import ru.vladsaybulin.model.anime.Video
import ru.vladsaybulin.model.character.CharacterWithRole
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.common.IncompleteDate
import ru.vladsaybulin.model.common.StatisticsItem
import ru.vladsaybulin.model.genre.Genre
import ru.vladsaybulin.model.genre.GenreKind
import ru.vladsaybulin.model.manga.MangaKind
import ru.vladsaybulin.model.manga.Publisher
import ru.vladsaybulin.model.person.PersonWithRoles
import ru.vladsaybulin.model.related.RelatedEntry
import ru.vladsaybulin.model.search.SearchType
import ru.vladsaybulin.model.userrate.UserRate
import ru.vladsaybulin.model.userrate.UserRateStatus

sealed class DetailsUiState {
    data object Loading : DetailsUiState()

    data class Error(val throwable: Throwable) : DetailsUiState()

    data class Success(
        val entryType: EntryType,
        val entryId: Long,
        val poster: Image?,
        val name: String,
        val russianName: String?,
        val status: EntryStatus,
        val animeKind: AnimeKind?,
        val mangaKind: MangaKind?,
        val score: Float,
        val episodes: Int,
        val episodesAired: Int,
        val episodeDuration: Int?,
        val chapters: Int,
        val volumes: Int,
        val nextEpisodeAt: Instant?,
        val airedOn: IncompleteDate?,
        val releasedOn: IncompleteDate?,
        val studios: ImmutableList<Studio>?,
        val publishers: ImmutableList<Publisher>?,
        val genres: ImmutableList<Genre>?,
        val descriptionHtml: String?,
        val descriptionSource: String?,
        val authors: ImmutableList<PersonWithRoles>?,
        val scoreStatisticsItems: List<StatisticsItem<Int>>?,
        val userRateStatusStatisticItems: List<StatisticsItem<UserRateStatus>>?,
        val related: ImmutableList<RelatedEntry>?,
        val characters: ImmutableList<CharacterWithRole>?,
        val screenshots: ImmutableList<Image>?,
        val videos: ImmutableList<Video>?,
        val similar: ImmutableList<SimilarEntry>,
        val userRate: UserRate?
    ) : DetailsUiState()
}

internal fun EntryDetails.toUiState(): DetailsUiState =
    if (anime != null) animeDetailsToUiState() else mangaDetailsToUiState()

private fun EntryDetails.animeDetailsToUiState() = with(anime!!) {
    DetailsUiState.Success(
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
        episodeDuration = duration?.takeIf { it > 0 },
        nextEpisodeAt = nextEpisodeAt,
        airedOn = airedOn,
        releasedOn = releasedOn,
        studios = studios?.toImmutableList(),
        genres = genres?.toImmutableList(),
        descriptionHtml = descriptionHtml,
        descriptionSource = descriptionSource,
        authors = authors?.toImmutableList(),
        scoreStatisticsItems = scoreStats,
        userRateStatusStatisticItems = userRateStatusStats?.filter { it.count > 0 },
        characters = characters?.toImmutableList(),
        related = related?.toImmutableList(),
        screenshots = screenshots.toImmutableList(),
        videos = videos?.toImmutableList(),
        similar = similarEntries.toImmutableList(),
        userRate = userRate,

        //Manga only fields
        chapters = 0,
        volumes = 0,
        mangaKind = null,
        publishers = null
    )
}

private fun EntryDetails.mangaDetailsToUiState() = with(manga!!) {
    DetailsUiState.Success(
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
        publishers = publishers.toImmutableList(),
        genres = genres?.toImmutableList(),
        descriptionHtml = descriptionHtml,
        descriptionSource = descriptionSource,
        authors = authors?.toImmutableList(),
        scoreStatisticsItems = scoreStats ?: emptyList(),
        userRateStatusStatisticItems = userRateStatusStats?.filter { it.count > 0 },
        characters = characters?.toImmutableList(),
        related = related?.toImmutableList(),
        similar = similarEntries.toImmutableList(),
        userRate = userRate,

        //Anime only fields
        episodes = 0,
        episodesAired = 0,
        episodeDuration = 0,
        nextEpisodeAt = null,
        animeKind = null,
        studios = null,
        screenshots = null,
        videos = null,
    )
}

internal fun DetailsUiState.Success.publisherSearchParams(publisherId: Long) =
    SearchArgs(
        searchType = if (entryType == EntryType.Anime) SearchType.Anime else SearchType.Manga,
        publisherId = publisherId
    )

internal fun DetailsUiState.Success.studioSearchParams(studioId: Long) =
    SearchArgs(
        searchType = if (entryType == EntryType.Anime) SearchType.Anime else SearchType.Manga,
        studioId = studioId
    )

internal fun DetailsUiState.Success.genreSearchParams(genre: Genre) =
    SearchArgs(
        searchType = if (entryType == EntryType.Anime) SearchType.Anime else SearchType.Manga,
        genreId = if (genre.kind == GenreKind.Genre) genre.id else null,
        demographicId = if (genre.kind == GenreKind.Demographic) genre.id else null,
        themeId = if (genre.kind == GenreKind.Theme) genre.id else null,
    )

internal fun DetailsUiState.Success.posterViewParams(): ImageViewArgs {
    require(poster != null)
    return ImageViewArgs(
        images = listOf(poster),
        initialIndex = 0,
        isSingle = true
    )
}

internal fun DetailsUiState.Success.screenshotViewParams(initialIndex: Int): ImageViewArgs {
    require(!screenshots.isNullOrEmpty())
    return ImageViewArgs(
        images = screenshots,
        initialIndex = initialIndex,
        isSingle = false
    )
}

