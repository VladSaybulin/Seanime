package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toJavaZoneId
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.navigation.SearchArgs
import ru.vladsaybulin.core.ui.LocalTimeZone
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.core.ui.strings.animeKindString
import ru.vladsaybulin.feature.details.DetailsUiState
import ru.vladsaybulin.feature.details.genreSearchParams
import ru.vladsaybulin.feature.details.studioSearchParams
import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.anime.Studio
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
    onGenreClick: (Long) -> Unit
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
        annotation = { it.id.toString() },
        onItemClick = { onStudioClick(it.toLong()) }
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
    val text = buildKindAndEpisodesString(
        kind = animeKind ?: AnimeKind.None,
        episodes = episodes,
        episodesAired = episodesAired,
        duration = episodeDuration,
        isOngoing = isOngoing
    ) ?: return

    InfoLine(
        icon = { InfoIcon(imageVector = ShikimoriIcons.Tv) },
        modifier = modifier
    ) {
        Text(text)
    }
}

@Composable
@ReadOnlyComposable
private fun buildKindAndEpisodesString(
    kind: AnimeKind,
    episodes: Int,
    episodesAired: Int,
    duration: Int?,
    isOngoing: Boolean
): String? {
    val kindText = animeKindString(animeKind = kind)
    val episodesText = buildEpisodesString(
        episodes = episodes,
        episodesAired = episodesAired,
        ongoing = isOngoing
    )
    val durationText = duration?.let {
        buildDurationString(it)
    }

    if (kindText == null && episodesText == null && durationText == null) return null

    return buildString {
        kindText?.let { append(it) }

        if (episodesText == null && durationText == null) return@buildString

        if (isNotEmpty()) append(", ")
        when {
            episodesText != null && durationText != null -> stringResource(
                id = R.string.episodes_x_duration,
                episodesText,
                durationText
            ).let { append(it) }

            episodesText != null -> stringResource(
                id = R.string.episodes,
                episodesText
            ).let { append(it) }

            else -> append(durationText)
        }
    }
}

@Composable
@ReadOnlyComposable
private fun buildDurationString(duration: Int): String {
    val hours = duration / 60
    val minutes = duration % 60

    val hoursText = hours.takeIf { it > 0 }?.let {
        pluralStringResource(id = R.plurals.duration_hours, count = it, it)
    }
    val minutesText = stringResource(id = R.string.duration_minutes, minutes)

    return buildString {
        hoursText?.let { append(it) }
        if (isNotEmpty()) append(' ')
        append(minutesText)
    }
}

@Composable
@ReadOnlyComposable
private fun buildEpisodesString(episodes: Int, episodesAired: Int, ongoing: Boolean): String? {
    if (episodes <= 0 && episodesAired <= 0) return null

    return when {
        ongoing && episodes > 0 && episodesAired > 0 -> "$episodesAired/$episodes"
        ongoing && episodesAired > 0 -> "$episodesAired/-"
        episodes > 0 -> episodes.toString()
        else -> null
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