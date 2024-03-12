package ru.vladsaybulin.feature.details

import ru.vladsaybulin.feature.details.model.DetailsHeader
import ru.vladsaybulin.feature.details.model.DetailsInfo
import ru.vladsaybulin.feature.details.model.SimilarEntry
import ru.vladsaybulin.feature.details.model.asSimilarEntry
import ru.vladsaybulin.model.Anime
import ru.vladsaybulin.model.AnimeDetails
import ru.vladsaybulin.model.AnimeKind
import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.UserRate
import ru.vladsaybulin.model.isNullOrEmpty

sealed class DetailsUiState {
    data object Loading : DetailsUiState()

    data class Error(val throwable: Throwable) : DetailsUiState()

    data class Success(
        val entryType: EntryType,
        val header: DetailsHeader,
        val info: List<DetailsInfo>,
        val userRate: UserRate?,
        val similar: List<SimilarEntry>,
    ) : DetailsUiState()
}

fun DetailsUiState(
    animeDetails: AnimeDetails,
    userRate: UserRate?,
    similar: List<Anime>
) = animeDetails.run {
    DetailsUiState.Success(
        entryType = EntryType.Anime,
        header = DetailsHeader(
            poster = poster,
            name = originalName,
            russianName = russianName,
            animeRating = rating
        ),
        info = buildList {
            if (shouldShowKindAndEpisodes()) {
                add(
                    DetailsInfo.AnimeKindEpisodes(
                        kind = kind,
                        episodes = episodes,
                        episodesAired = episodesAired,
                        duration = duration,
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
        userRate = userRate,
        similar = similar.map(Anime::asSimilarEntry)
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