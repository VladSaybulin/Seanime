package ru.vladsaybulin.feature.details

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Instant
import ru.vladsaybulin.core.navigation.ImageViewArgs
import ru.vladsaybulin.core.navigation.SearchArgs
import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.character.CharacterWithRole
import ru.vladsaybulin.model.EntryDetails
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.genre.Genre
import ru.vladsaybulin.model.common.IncompleteDate
import ru.vladsaybulin.model.manga.MangaKind
import ru.vladsaybulin.model.person.PersonWithRoles
import ru.vladsaybulin.model.common.Poster
import ru.vladsaybulin.model.manga.Publisher
import ru.vladsaybulin.model.related.RelatedEntry
import ru.vladsaybulin.model.anime.Screenshot
import ru.vladsaybulin.model.SimilarEntry
import ru.vladsaybulin.model.anime.Studio
import ru.vladsaybulin.model.userrate.UserRate
import ru.vladsaybulin.model.anime.Video

sealed class DetailsUiState {
    data object Loading : DetailsUiState()

    data class Error(val throwable: Throwable) : DetailsUiState()

    data class Success(
        val entryType: EntryType,
        val entryId: Long,
        val poster: Poster?,
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
        val related: ImmutableList<RelatedEntry>?,
        val characters: ImmutableList<CharacterWithRole>?,
        val screenshots: ImmutableList<Screenshot>?,
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
        score = score ?: 0f,
        episodes = episodes,
        episodesAired = episodesAired,
        episodeDuration = duration?.takeIf { it > 0 },
        nextEpisodeAt = nextEpisodeAt,
        airedOn = airedOn,
        releasedOn = releasedOn,
        studios = studios.toImmutableList(),
        genres = genres?.toImmutableList(),
        descriptionHtml = descriptionHtml,
        descriptionSource = descriptionSource,
        authors = authors?.toImmutableList(),
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
        entryType = entryType,
        publisherId = publisherId
    )

internal fun DetailsUiState.Success.studioSearchParams(studioId: Long) =
    SearchArgs(
        entryType = entryType,
        studioId = studioId
    )

internal fun DetailsUiState.Success.genreSearchParams(genreId: Long) =
    SearchArgs(
        entryType = entryType,
        genreId = genreId
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

