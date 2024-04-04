package ru.vladsaybulin.feature.details

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ru.vladsaybulin.feature.details.model.DetailsDescription
import ru.vladsaybulin.feature.details.model.DetailsInfo
import ru.vladsaybulin.model.AnimeDetails
import ru.vladsaybulin.model.AnimeKind
import ru.vladsaybulin.model.CharacterWithRole
import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.PersonWithRoles
import ru.vladsaybulin.model.Poster
import ru.vladsaybulin.model.RelatedEntry
import ru.vladsaybulin.model.Screenshot
import ru.vladsaybulin.model.SimilarEntry
import ru.vladsaybulin.model.UserRate
import ru.vladsaybulin.model.Video
import ru.vladsaybulin.model.isNullOrEmpty

sealed class DetailsUiState {
    data object Loading : DetailsUiState()

    data class Error(val throwable: Throwable) : DetailsUiState()

    data class Success(
        val entryType: EntryType,
        val poster: Poster?,
        val name: String,
        val russianName: String?,
        val status: EntryStatus,
        val info: List<DetailsInfo>,
        val userRate: UserRate?,
        val description: DetailsDescription?,
        val authors: ImmutableList<PersonWithRoles>?,
        val related: ImmutableList<RelatedEntry>?,
        val characters: ImmutableList<CharacterWithRole>?,
        val screenshots: ImmutableList<Screenshot>?,
        val videos: ImmutableList<Video>?,
        val similar: ImmutableList<SimilarEntry>?
    ) : DetailsUiState()
}

fun DetailsUiState(
    animeDetails: AnimeDetails,
    userRate: UserRate?,
    similar: List<SimilarEntry>
) = animeDetails.run {
    DetailsUiState.Success(
        entryType = EntryType.Anime,
        poster = poster,
        name = originalName,
        russianName = russianName,
        status = status,
        info = buildList {
            if (shouldShowKindAndEpisodes()) {
                add(
                    DetailsInfo.AnimeKindEpisodes(
                        kind = kind,
                        episodes = episodes,
                        episodesAired = episodesAired,
                        duration = duration?.takeIf { it > 0 },
                        ongoing = status == EntryStatus.Ongoing
                    )
                )
            }
            if (shouldShowNextEpisode()) {
                add(DetailsInfo.NextEpisode(nextEpisodeDate = nextEpisodeAt!!))
            }
            if (shouldShowStatusAndDates()) {
                add(
                    DetailsInfo.StatusDates(
                        entryType = EntryType.Anime,
                        status = status,
                        airedOn = airedOn,
                        releasedOn = releasedOn
                    )
                )
            }
            if (studios.isNotEmpty()) {
                add(DetailsInfo.Studios(studios))
            }
            if (!genres.isNullOrEmpty()) {
                add(DetailsInfo.Genres(genres!!, R.string.header_genres, key = "genres"))
            }
        },
        description = if (!descriptionHtml.isNullOrBlank()) {
            DetailsDescription(
                code = descriptionHtml!!,
                source = descriptionSource
            )
        } else null,
        authors = authors?.ifEmpty { null }?.toImmutableList(),
        characters = characters?.ifEmpty { null }?.toImmutableList(),
        related = related?.ifEmpty { null }?.toImmutableList(),
        screenshots = screenshots.ifEmpty { null }?.toImmutableList(),
        videos = videos?.ifEmpty { null }?.toImmutableList(),
        similar = similar.ifEmpty { null }?.toImmutableList(),
        userRate = userRate,
    )
}

fun AnimeDetails.shouldShowKindAndEpisodes() =
    kind != AnimeKind.None ||
            episodes > 1 ||
            duration != null

fun AnimeDetails.shouldShowNextEpisode() =
    nextEpisodeAt != null &&
            status == EntryStatus.Ongoing

fun AnimeDetails.shouldShowStatusAndDates() =
    status != EntryStatus.None
            || airedOn.isNullOrEmpty()
            || releasedOn.isNullOrEmpty()

