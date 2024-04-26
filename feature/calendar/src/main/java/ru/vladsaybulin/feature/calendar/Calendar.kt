package ru.vladsaybulin.feature.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toJavaZoneId
import kotlinx.datetime.toLocalDateTime
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.domain.CalendarDay
import ru.vladsaybulin.core.navigation.args.EntryDetailsArgs
import ru.vladsaybulin.core.ui.FullScreenErrorMessage
import ru.vladsaybulin.core.ui.LocalScreenContentPadding
import ru.vladsaybulin.core.ui.anime.AnimeCarousel
import ru.vladsaybulin.model.calendar.CalendarItem
import ru.vladsaybulin.model.calendar.previewCalendarItems
import ru.vladsaybulin.model.common.EntryType
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun CalendarRoute(
    onEntryClick: (EntryDetailsArgs) -> Unit,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CalendarScreen(
        uiState = uiState,
        onAnimeClick = { onEntryClick(EntryDetailsArgs(EntryType.Anime, it)) },
        onRefresh = viewModel::forceRefresh
    )
}

@Composable
fun CalendarScreen(
    uiState: CalendarUiState,
    onAnimeClick: (animeId: Long) -> Unit = {},
    onRefresh: suspend () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(LocalScreenContentPadding.current)
            .fillMaxSize()
    ) {
        when (uiState) {
            is CalendarUiState.Error -> FullScreenErrorMessage(throwable = uiState.throwable)
            CalendarUiState.Loading -> CalendarLoading(modifier = Modifier.fillMaxSize())
            is CalendarUiState.Success -> CalendarContent(
                calendarDays = uiState.calendarDays,
                onCalendarItemClick = { onAnimeClick(it.anime.id) },
                onRefresh = onRefresh
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarContent(
    modifier: Modifier = Modifier,
    calendarDays: List<CalendarDay>,
    onCalendarItemClick: (CalendarItem) -> Unit,
    onRefresh: suspend () -> Unit
) {
    val pullToRefreshState = rememberPullToRefreshState()
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            onRefresh()
            pullToRefreshState.endRefresh()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(pullToRefreshState.nestedScrollConnection),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp + WindowInsets.statusBars.asPaddingValues().calculateTopPadding(), bottom = 16.dp)
    ) {
        items(calendarDays) { calendarDay ->
            CalendarSection(
                calendarDay = calendarDay,
                onCalendarItemClick = onCalendarItemClick
            )
        }
    }
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        PullToRefreshContainer(state = pullToRefreshState)
    }

}

@Composable
private fun CalendarLoading(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun CalendarSection(
    calendarDay: CalendarDay,
    modifier: Modifier = Modifier,
    onCalendarItemClick: (CalendarItem) -> Unit
) {
    Column(modifier = modifier) {
        CalendarSectionHeader(date = calendarDay.date)
        Spacer(modifier = Modifier.height(4.dp))

        AnimeCarousel(
            items = calendarDay.items,
            mapAnime = { it.anime },
            onClick = onCalendarItemClick,
            metadata = {
                CalendarItemDetails(
                    nextEpisodeAt = it.nextEpisodeAt,
                    wasOnAir = calendarDay.date == null,
                    nextEpisode = it.nextEpisode
                )
            }
        )
    }
}

@Composable
private fun CalendarSectionHeader(date: LocalDate?) {
    val headerText = if (date == null) {
        stringResource(id = R.string.was_on_aired)
    } else {
        val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
            .withZone(TimeZone.currentSystemDefault().toJavaZoneId())
        formatter.format(date.toJavaLocalDate())
    }

    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(text = headerText, style = ShikimoriTheme.typography.titleMedium)
    }
}

@Composable
private fun CalendarItemDetails(
    nextEpisode: Int,
    nextEpisodeAt: Instant,
    wasOnAir: Boolean = false,
) {
    val localDateTime = nextEpisodeAt.toLocalDateTime(TimeZone.currentSystemDefault())
    val timeText = if (!wasOnAir) {
        DateTimeFormatter.ofPattern("HH:mm").format(localDateTime.time.toJavaLocalTime())
    } else {
        val nowLocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        when (nowLocalDate.toEpochDays() - localDateTime.date.toEpochDays()) {
            0 -> stringResource(id = R.string.today)
            1 -> stringResource(id = R.string.yesterday)
            else -> {
                val formatterPattern = stringResource(id = R.string.date_formatter_pattern)
                val formatter = DateTimeFormatter.ofPattern(formatterPattern)
                formatter.format(localDateTime.date.toJavaLocalDate())
            }
        }
    }

    val episodeText = if (nextEpisode > 0) {
        stringResource(id = R.string.episode, nextEpisode)
    } else null

    val text = buildString {
        if (episodeText != null) {
            append(episodeText)
            append(" \u00B7 ")
        }
        append(timeText)
    }

    Text(text = text)
}

class CalendarPreviewProvider : PreviewParameterProvider<CalendarItem> {
    override val values: Sequence<CalendarItem>
        get() = previewCalendarItems.asSequence()

}

@Preview
@Composable
fun CalendarSectionPreview() {
    ShikimoriTheme {
        Surface {
            CalendarSection(
                modifier = Modifier.width(400.dp),
                calendarDay = CalendarDay(
                    items = previewCalendarItems,
                    date = Clock.System.now()
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                        .date
                ),
                onCalendarItemClick = { }
            )
        }
    }
}

@Preview
@Composable
fun CalendarLoadingPreview() {
    ShikimoriTheme {
        Surface {
            CalendarLoading(modifier = Modifier.fillMaxSize())
        }
    }
}