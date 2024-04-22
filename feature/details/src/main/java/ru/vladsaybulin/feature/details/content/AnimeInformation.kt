package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toJavaZoneId
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.navigation.args.SearchArgs
import ru.vladsaybulin.core.ui.LocalTimeZone
import ru.vladsaybulin.core.ui.anime.AnimeInfoKindAndEpisodesAndDurationText
import ru.vladsaybulin.feature.details.DetailsUiState
import ru.vladsaybulin.feature.details.genreSearchParams
import ru.vladsaybulin.feature.details.studioSearchParams
import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.anime.Studio
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.genre.Genre
import java.time.format.DateTimeFormatter
import java.util.Locale

internal fun LazyListScope.animeInformation(
    state: DetailsUiState.Success,
    onSearchClick: (SearchArgs) -> Unit,
) {
    item(key = "anime_info") {
        AnimeInformation(
            state = state,
            onStudioClick = { onSearchClick(state.studioSearchParams(it)) },
            onGenreClick = { onSearchClick(state.genreSearchParams(it)) }
        )
    }
}

@Composable
private fun AnimeInformation(
    state: DetailsUiState.Success,
    onStudioClick: (Long) -> Unit,
    onGenreClick: (Genre) -> Unit
) {
    AnimeKindAndEpisodeInfoLine(
        animeKind = state.animeKind,
        episodes = state.episodes,
        episodesAired = state.episodesAired,
        episodeDuration = state.episodeDuration,
        isOngoing = state.status == EntryStatus.Ongoing
    )

    StatusAndDatesInfoLine(
        airedOn = state.airedOn,
        releasedOn = state.releasedOn,
        status = state.status
    )

    if (state.nextEpisodeAt != null) {
        NextEpisodeDateInfoLine(nextEpisodeAt = state.nextEpisodeAt)
    }

    if (!state.studios.isNullOrEmpty()) {
        StudiosLineInfo(
            studios = state.studios,
            onStudioClick = onStudioClick
        )
    }

    if (!state.genres.isNullOrEmpty()) {
        GenresInfoLine(
            genres = state.genres,
            onGenreClick = onGenreClick
        )
    }
}

@Composable
private fun StudiosLineInfo(
    studios: ImmutableList<Studio>,
    onStudioClick: (Long) -> Unit
) {
    ListedInformation(
        items = studios,
        labelSingleStringRes = ru.vladsaybulin.feature.details.R.string.studios,
        labelSeveralStringRes = ru.vladsaybulin.feature.details.R.string.single_studio,
        name = { it.name },
        onItemClick = { onStudioClick(it.id) }
    )
}

@Composable
private fun NextEpisodeDateInfoLine(nextEpisodeAt: Instant) {
    InfoLine(
        icon = { InfoIcon(imageVector = ShikimoriIcons.AccessTime) }
    ) {
        Text(text = dateFormatted(date = nextEpisodeAt))
    }
}

@Composable
private fun AnimeKindAndEpisodeInfoLine(
    animeKind: AnimeKind?,
    episodes: Int,
    episodesAired: Int,
    episodeDuration: Int?,
    isOngoing: Boolean,
    modifier: Modifier = Modifier
) {
    InfoLine(
        icon = { InfoIcon(imageVector = ShikimoriIcons.Tv) },
        modifier = modifier
    ) {
        AnimeInfoKindAndEpisodesAndDurationText(
            kind = animeKind ?: AnimeKind.None,
            episodes = episodes,
            episodesAired = episodesAired,
            duration = episodeDuration ?: 0,
            isOngoing = isOngoing
        )
    }
}

@Composable
private fun dateFormatted(date: Instant): String {
    val timeZone = LocalTimeZone.current
    val now = Clock.System.todayIn(timeZone)
    val localDateTime = date.toLocalDateTime(timeZone)
    val pattern = if (now.year == localDateTime.date.year) {
        stringResource(id = ru.vladsaybulin.feature.details.R.string.next_episode_at_pattern)
    } else {
        stringResource(id = ru.vladsaybulin.feature.details.R.string.next_episode_at_pattern_with_year)
    }
    return DateTimeFormatter
        .ofPattern(pattern)
        .withLocale(Locale.getDefault())
        .withZone(timeZone.toJavaZoneId())
        .format(localDateTime.toJavaLocalDateTime())
}