/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vladsaybulin.feature.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toLocalDateTime
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.designsystem.theme.get
import ru.vladsaybulin.core.domain.calendar.CalendarDay
import ru.vladsaybulin.core.ui.FullScreenErrorMessage
import ru.vladsaybulin.core.ui.LocalScreenContentPadding
import ru.vladsaybulin.core.ui.ProfileButton
import ru.vladsaybulin.core.ui2.entry.EntryCarousel
import ru.vladsaybulin.core.ui2.entry.anime.AnimeGridItem
import ru.vladsaybulin.feature.calendar.navigation.CalendarNavEvents
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.calendar.previewCalendarItems
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.user.BriefUser
import java.time.format.DateTimeFormatter
import ru.vladsaybulin.core.ui.R as uiR

@Composable
fun CalendarRoute(
    navEvents: CalendarNavEvents,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val me by viewModel.me.collectAsStateWithLifecycle()

    CalendarScreen(
        uiState = uiState,
        me = me,
        navEvents = navEvents,
        onRefresh = viewModel::forceRefresh
    )
}

@Composable
fun CalendarScreen(
    uiState: CalendarUiState,
    me: BriefUser?,
    navEvents: CalendarNavEvents,
    onRefresh: suspend () -> Unit = {},
) {
    Scaffold(
        modifier = Modifier.padding(LocalScreenContentPadding.current),
        topBar = {
            CalendarTopBar(me = me, navigateToMe = navEvents.navigateToMe)
        }
    ) { scaffoldPadding ->
        Box(modifier = Modifier.padding(scaffoldPadding)) {
            CalendarContent(
                state = uiState,
                navigateToAnimeDetails = navEvents.navigateToAnimeDetails,
                onRefresh = onRefresh
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalendarTopBar(
    me: BriefUser?,
    navigateToMe: () -> Unit
) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.feature_calendar_title)) },
        actions = {
            ProfileButton(image = me?.image, onClick = navigateToMe)
        }
    )
}

@Composable
private fun CalendarContent(
    state: CalendarUiState,
    navigateToAnimeDetails: (Long) -> Unit,
    onRefresh: suspend () -> Unit
) {
    when (state) {
        is CalendarUiState.Error -> FullScreenErrorMessage(throwable = state.throwable)
        CalendarUiState.Loading -> CalendarLoadingBody(modifier = Modifier.fillMaxSize())
        is CalendarUiState.Success -> CalendarBody(
            calendarDays = state.calendarDays,
            onAnimeClick = { navigateToAnimeDetails(it.id) },
            onRefresh = onRefresh
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarBody(
    modifier: Modifier = Modifier,
    calendarDays: List<CalendarDay>,
    onAnimeClick: (Anime) -> Unit,
    onRefresh: suspend () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val isRefreshing = remember { mutableStateOf(false) }

    PullToRefreshBox(
        isRefreshing = isRefreshing.value,
        onRefresh = {
            coroutineScope.launch {
                isRefreshing.value = true
                onRefresh()
                isRefreshing.value = false
            }
        }
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                top = 16.dp,
                bottom = 16.dp
            )
        ) {
            items(calendarDays) { calendarDay ->
                CalendarSection(
                    calendarDay = calendarDay,
                    onAnimeClick = onAnimeClick
                )
            }
        }
    }
}

@Composable
private fun CalendarLoadingBody(modifier: Modifier) {
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
    onAnimeClick: (Anime) -> Unit
) {
    Column(modifier = modifier) {
        CalendarSectionHeader(date = calendarDay.date)
        Spacer(modifier = Modifier.height(4.dp))

        EntryCarousel {
            items(
                items = calendarDay.items,
                key = { it.anime.id }
            ) { calendarItem ->
                AnimeGridItem(
                    anime = calendarItem.anime,
                    onClick = { onAnimeClick(calendarItem.anime) },
                    modifier = Modifier.width(CalendarItemWidth),
                    additionalContent = {
                        CalendarItemDetails(
                            nextEpisodeAt = calendarItem.nextEpisodeAt,
                            wasOnAir = calendarDay.date == null,
                            nextEpisode = calendarItem.nextEpisode
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun CalendarSectionHeader(date: LocalDate?) {
    val headerText = if (date == null) {
        stringResource(id = R.string.was_on_aired)
    } else {
        val weekNames = DayOfWeekNames(stringArrayResource(id = uiR.array.core_ui_week_names).toList())
        val monthNames = MonthNames(stringArrayResource(id = uiR.array.core_ui_month_names_in_nominative_case).toList())
        val formatter = LocalDate.Format {
            dayOfWeek(weekNames)
            chars(", ")
            dayOfMonth()
            chars(" ")
            monthName(monthNames)
        }
        formatter.format(date).replaceFirstChar { it.uppercase() }
    }

    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(text = headerText, style = SeanimeTheme.typography.titleMedium)
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

    val isAnons = nextEpisode <= 1

    val episodeText = when (isAnons) {
        true -> stringResource(id = R.string.feature_calendar_anons)
        else -> stringResource(id = R.string.feature_calendar_episode, nextEpisode)
    }

    val anonsColor = SeanimeTheme.seanimeColors[EntryStatus.Anons]

    val text = buildAnnotatedString {
        val index = if (isAnons) {
            pushStyle(SpanStyle(color = anonsColor))
        } else null
        append(episodeText)
        index?.let { pop(it) }
        append(" \u00B7 ")
        append(timeText)
    }

    Text(text = text, style = SeanimeTheme.typography.bodySmall)
}

@Preview
@Composable
fun CalendarSectionPreview() {
    SeanimeTheme {
        Surface {
            CalendarSection(
                modifier = Modifier.width(400.dp),
                calendarDay = CalendarDay(
                    items = previewCalendarItems,
                    date = Clock.System.now()
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                        .date
                ),
                onAnimeClick = { }
            )
        }
    }
}

@Preview
@Composable
fun CalendarLoadingPreview() {
    SeanimeTheme {
        Surface {
            CalendarLoadingBody(modifier = Modifier.fillMaxSize())
        }
    }
}

private val CalendarItemWidth = 128.dp