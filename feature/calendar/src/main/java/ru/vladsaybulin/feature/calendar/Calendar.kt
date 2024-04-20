package ru.vladsaybulin.feature.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
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
import ru.vladsaybulin.core.ui.anime.AnimeGridItem
import ru.vladsaybulin.model.calendar.CalendarItem
import ru.vladsaybulin.model.calendar.previewCalendarItems
import ru.vladsaybulin.model.common.EntryType
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun CalendarRoute(
    onEntryClick: (EntryDetailsArgs) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = hiltViewModel(),
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
            .padding(bottom = 80.dp)
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
        modifier = modifier.nestedScroll(pullToRefreshState.nestedScrollConnection),
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
fun CalendarError(
    modifier: Modifier = Modifier,
    errorState: CalendarUiState.Error
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.error_message_title),
            style = ShikimoriTheme.typography.titleLarge
        )
        Text(
            text = stringResource(id = R.string.error_message),
            style = ShikimoriTheme.typography.bodyMedium
        )
        errorState.throwable.message?.let {
            Text(
                text = it,
                color = LocalContentColor.current.copy(alpha = 0.5f),
                style = ShikimoriTheme.typography.bodySmall
            )
        }
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
private fun CalendarGridItem(
    modifier: Modifier = Modifier,
    calendarItem: CalendarItem,
    wasOnAir: Boolean = false,
    onClick: () -> Unit
) {
    AnimeGridItem(
        modifier = modifier,
        anime = calendarItem.anime,
        onClick = onClick,
        detailsContent = {
            CalendarItemDetails(
                nextEpisodeAt = calendarItem.nextEpisodeAt,
                wasOnAir = wasOnAir,
                nextEpisode = calendarItem.nextEpisode
            )
        }
    )
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
        CalendarSectionItemsCarousel(
            calendarItems = calendarDay.items,
            wasOnAir = calendarDay.date == null,
            onItemClick = onCalendarItemClick
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CalendarSectionItemsCarousel(
    modifier: Modifier = Modifier,
    calendarItems: List<CalendarItem>,
    wasOnAir: Boolean = false,
    onItemClick: (CalendarItem) -> Unit
) {
    val listState = rememberLazyListState()

    LazyRow(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        flingBehavior = rememberSnapFlingBehavior(
            snapLayoutInfoProvider = remember(listState) {
                SnapLayoutInfoProvider(
                    lazyListState = listState,
                    positionInLayout = { _, _, _, _, _ -> 0 },
                )
            },
        ),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = calendarItems,
            key = { it.nextEpisodeAt.toString() + it.anime.id.toString() }
        ) { calendarItem ->
            CalendarGridItem(
                modifier = Modifier.width(160.dp),
                calendarItem = calendarItem,
                wasOnAir = wasOnAir,
                onClick = { onItemClick(calendarItem) }
            )
        }
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

@Preview
@Composable
fun CalendarItemPreview(@PreviewParameter(CalendarPreviewProvider::class) calendarItem: CalendarItem) {
    ShikimoriTheme {
        CalendarGridItem(
            modifier = Modifier.width(128.dp),
            calendarItem = calendarItem,
            onClick = { }
        )
    }
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
fun CalendarErrorPreview() {
    ShikimoriTheme {
        Surface {
            CalendarError(
                modifier = Modifier.fillMaxSize(),
                errorState = CalendarUiState.Error(Exception("Сообщение об ошибке"))
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