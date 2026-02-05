package ru.vladsaybulin.feature.title.details.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.format.format
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toJavaZoneId
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.designsystem.theme.get
import ru.vladsaybulin.core.ui.strings.LocalTitleStrings
import ru.vladsaybulin.core.ui2.strings.AnimeStrings
import ru.vladsaybulin.core.ui2.strings.compose.asString
import ru.vladsaybulin.core.ui2.strings.compose.asStringOrNull
import ru.vladsaybulin.feature.title.details.R
import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.anime.AnimeRating
import ru.vladsaybulin.model.anime.Studio
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.IncompleteDate
import ru.vladsaybulin.model.common.isNullOrEmpty
import ru.vladsaybulin.model.genre.Genre
import ru.vladsaybulin.model.manga.MangaKind
import ru.vladsaybulin.model.manga.Publisher
import ru.vladsaybulin.model.search.TimePeriodAiring
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TitleInfo(
    animeKind: AnimeKind,
    mangaKind: MangaKind,
    status: EntryStatus,
    episodes: Int,
    episodesAired: Int,
    episodeDuration: Int,
    chapters: Int,
    volumes: Int,
    nextEpisodeAt: Instant?,
    airedOn: IncompleteDate?,
    releasedOn: IncompleteDate?,
    season: TimePeriodAiring.Season?,
    rating: AnimeRating,
    studios: List<Studio>,
    publishers: List<Publisher>,
    genres: List<Genre>,
    onStudioClick: (Studio) -> Unit,
    onPublisherClick: (Publisher) -> Unit,
    onGenreClick: (Genre) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            animeKind.asStringOrNull()?.let {
                KindPanel(kindString = it)
            }

            mangaKind.asStringOrNull()?.let {
                KindPanel(kindString = it)
            }

            status.asStringOrNull()?.let {
                StatusPanel(status = status)
            }

            val episodesFormat = AnimeStrings.getProgressFormat(
                aired = episodesAired,
                total = episodes,
                isOngoing = status == EntryStatus.Ongoing,
                isMovie = animeKind == AnimeKind.Movie
            )

            episodesFormat?.let {
                EpisodesPanel(it)
            }

            if (episodeDuration != 0) {
                EpisodeDurationPanel(
                    duration = episodeDuration,
                    isSingleEpisode = episodesFormat == null
                )
            }

            if (chapters != 0) {
                ChaptersPanel(chapters = chapters)
            }

            if (volumes != 0) {
                VolumesPanel(volumes = volumes)
            }

            when {
                status == EntryStatus.Anons && airedOn != null -> AirDatePanel(airedOn)
                season != null -> SeasonPanel(
                    timePeriodAiring = season,
                    airedOn = airedOn,
                    releasedOn = releasedOn
                )

                airedOn?.year != null || releasedOn?.year != null -> AirYearPanel(
                    airedOn = airedOn,
                    releasedOn = releasedOn
                )
            }

            if (rating != AnimeRating.None) {
                RatingPanel(rating = rating)
            }

            if (nextEpisodeAt != null) {
                NextEpisodeDatePanel(nextEpisodeAt = nextEpisodeAt)
            }
        }

        if (studios.isNotEmpty()) {
            StudiosPanel(studios = studios, onStudioClick = onStudioClick)
        }

        if (publishers.isNotEmpty()) {
            PublishersPanel(publishers = publishers, onPublisherClick = onPublisherClick)
        }

        if (genres.isNotEmpty()) {
            GenresPanel(genres = genres, onGenreClick = onGenreClick)
        }

    }
}

@Composable
private fun KindPanel(kindString: String) {
    InfoPanel(
        label = { Text(stringResource(id = R.string.feature_details_info_label_kind)) }
    ) {
        Text(text = kindString)
    }
}

@Composable
private fun StatusPanel(status: EntryStatus) {
    InfoPanel(
        label = { Text(stringResource(id = R.string.feature_details_info_label_status)) }
    ) {
        Text(
            text = status.asString(),
            color = SeanimeTheme.seanimeColors[status]
        )
    }
}

@Composable
private fun EpisodesPanel(format: AnimeStrings.ProgressFormat) {
    InfoPanel(
        label = { Text(stringResource(id = R.string.feature_details_info_label_episodes)) }
    ) {
        Text(text = format.asString())
    }
}

@Composable
private fun EpisodeDurationPanel(duration: Int, isSingleEpisode: Boolean) {
    InfoPanel(
        label = { Text(stringResource(id = R.string.feature_details_info_label_episode_duration)) }
    ) {

        val hours = duration / 60
        val hoursText = when {
            hours == 0 -> null

            isSingleEpisode -> pluralStringResource(
                id = R.plurals.feature_details_info_duration_hours_in_normative_case,
                count = hours,
                hours
            )

            else -> pluralStringResource(
                id = R.plurals.feature_details_info_duration_by_hours_in_genitive_case,
                count = hours,
                hours
            )
        }

        val minutes = duration % 60
        val minutesText =
            stringResource(id = R.string.feature_details_info_duration_minutes, minutes)

        Text(
            text = when {
                isSingleEpisode && hoursText != null -> stringResource(
                    id = R.string.feature_details_info_duration_when_is_single_episode_in_hours,
                    hoursText,
                    minutesText
                )

                isSingleEpisode -> stringResource(
                    id = R.string.feature_details_info_duration_when_is_single_episode_in_minutes,
                    minutesText
                )

                hoursText != null -> stringResource(
                    id = R.string.feature_details_info_duration_when_is_not_single_episode_in_hours,
                    hoursText,
                    minutesText
                )

                else -> stringResource(
                    id = R.string.feature_details_info_duration_when_is_not_single_episode_in_minutes,
                    minutesText
                )
            }
        )
    }
}

@Composable
private fun ChaptersPanel(chapters: Int) {
    InfoPanel(
        label = { Text(stringResource(id = R.string.feature_details_info_label_chapters)) }
    ) {
        Text(text = chapters.toString())
    }
}

@Composable
private fun VolumesPanel(volumes: Int) {
    InfoPanel(
        label = { Text(stringResource(id = R.string.feature_details_info_label_volumes)) }
    ) {
        Text(text = volumes.toString())
    }
}

@Composable
fun AirDatePanel(airedOn: IncompleteDate) {
    val labelId = when (LocalTitleStrings.current) {
        EntryType.Anime -> R.string.feature_title_details_info_label_anime_anons_date
        EntryType.Manga -> R.string.feature_title_details_info_label_manga_anons_data
    }
    InfoPanel(
        label = { Text(stringResource(id = labelId)) }
    ) {
        Text(text = incompleteDateFormatted(incompleteDate = airedOn))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeasonPanel(
    timePeriodAiring: TimePeriodAiring.Season,
    airedOn: IncompleteDate?,
    releasedOn: IncompleteDate?
) {

    val tooltipState = rememberTooltipState(isPersistent = true)
    val scope = rememberCoroutineScope()

    AirDateTooltipBox(tooltipState = tooltipState, airedOn = airedOn, releasedOn = releasedOn) {
        InfoPanel(
            label = {
                WithInfoIconLabel { Text(stringResource(id = R.string.feature_details_info_label_season)) }
            },
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    scope.launch { tooltipState.show() }
                }
            )
        ) {
            val seasonText =
                stringArrayResource(id = R.array.feature_details_info_seasons)[timePeriodAiring.seasonOfYear.ordinal]
            Text(
                text = "$seasonText ${timePeriodAiring.year} г."
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AirYearPanel(airedOn: IncompleteDate?, releasedOn: IncompleteDate?) {
    val tooltipState = rememberTooltipState(isPersistent = true)
    val scope = rememberCoroutineScope()

    AirDateTooltipBox(tooltipState = tooltipState, airedOn = airedOn, releasedOn = releasedOn) {
        InfoPanel(
            label = {
                WithInfoIconLabel { Text(text = stringResource(id = R.string.feature_details_info_label_air_year)) }
            },
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    scope.launch { tooltipState.show() }
                }
            )
        ) {
            val year = checkNotNull(airedOn?.year ?: releasedOn?.year)
            Text(text = stringResource(id = R.string.feature_details_info_air_year, year))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AirDateTooltipBox(
    tooltipState: TooltipState,
    airedOn: IncompleteDate?,
    releasedOn: IncompleteDate?,
    panelContent: @Composable () -> Unit
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            RichTooltip {
                AirDateTooltipContent(airedOn = airedOn, releasedOn = releasedOn)
            }
        },
        state = tooltipState,
        content = panelContent
    )
}

@Composable
private fun AirDateTooltipContent(airedOn: IncompleteDate?, releasedOn: IncompleteDate?) {
    val endAiredOn = if (airedOn != releasedOn && !airedOn.isNullOrEmpty()) {
        if (airedOn!!.year!! == releasedOn?.year) {
            airedOn.copy(year = null).takeIf { it.isNullOrEmpty() }
        } else airedOn
    } else null

    Text(
        text = when {
            endAiredOn != null && releasedOn != null -> {
                stringResource(
                    id = R.string.feature_details_info_date_range,
                    incompleteDateFormatted(endAiredOn),
                    incompleteDateFormatted(releasedOn, MonthCase.Genitive)
                )
            }

            endAiredOn != null -> {
                stringResource(
                    id = R.string.feature_details_info_date_aired_on,
                    incompleteDateFormatted(endAiredOn)
                )
            }

            releasedOn != null -> {
                incompleteDateFormatted(releasedOn)
            }

            else -> "No data"
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RatingPanel(rating: AnimeRating) {
    val tooltipState = rememberTooltipState(isPersistent = true)
    val scope = rememberCoroutineScope()

    TooltipBox(
        positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
        tooltip = {
            RichTooltip { Text(text = animeRatingDescription(rating = rating)) }
        },
        state = tooltipState
    ) {
        InfoPanel(
            label = { WithInfoIconLabel { Text(stringResource(id = R.string.feature_details_info_label_rating)) } },
            modifier = Modifier.clickable { scope.launch { tooltipState.show() } }
        ) {
            Text(text = rating.asString())
        }
    }

}

@Composable
private fun NextEpisodeDatePanel(nextEpisodeAt: Instant) {
    InfoPanel(
        label = { Text(stringResource(id = R.string.feature_details_info_label_next_episode)) }
    ) {
        Text(
            text = DateTimeFormatter.ofPattern("dd MMM, hh:mm")
                .withLocale(java.util.Locale.getDefault())
                .withZone(TimeZone.currentSystemDefault().toJavaZoneId())
                .format(nextEpisodeAt.toJavaInstant())
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StudiosPanel(
    studios: List<Studio>,
    onStudioClick: (Studio) -> Unit
) {
    InfoPanel(
        label = { Text(stringResource(id = R.string.feature_details_info_label_studios)) }
    ) {
        FlowRow(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            studios.forEach { studio ->
                ElevatedSuggestionChip(
                    onClick = { onStudioClick(studio) },
                    label = { Text(text = studio.name) }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PublishersPanel(
    publishers: List<Publisher>,
    onPublisherClick: (Publisher) -> Unit
) {
    InfoPanel(
        label = { Text(stringResource(id = R.string.feature_details_info_label_publishers)) }
    ) {
        FlowRow(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            publishers.forEach { publisher ->
                ElevatedSuggestionChip(
                    onClick = { onPublisherClick(publisher) },
                    label = { Text(text = publisher.name) })
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GenresPanel(
    genres: List<Genre>,
    onGenreClick: (Genre) -> Unit
) {
    InfoPanel(
        label = { Text(text = stringResource(id = R.string.feature_details_info_label_genres)) }
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            genres.forEach { genre ->
                ElevatedSuggestionChip(
                    onClick = { onGenreClick(genre) },
                    label = { Text(text = genre.russianName ?: genre.englishName) })

            }
        }
    }
}

@Composable
private fun InfoPanel(
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        Box(modifier = Modifier.alpha(0.5f)) {
            ProvideTextStyle(value = SeanimeTheme.typography.labelSmall) {
                label()
            }
        }

        ProvideTextStyle(SeanimeTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)) {
            content()
        }
    }
}

@Composable
private fun WithInfoIconLabel(label: @Composable () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        label()
        Icon(
            imageVector = SeanimeIcons.OutlinedInfo,
            contentDescription = stringResource(id = R.string.feature_details_info_label_kind),
            modifier = Modifier
                .padding(start = 4.dp)
                .size(16.dp)
        )
    }
}

@Composable
@ReadOnlyComposable
private fun incompleteDateFormatted(
    incompleteDate: IncompleteDate,
    monthCase: MonthCase = MonthCase.Auto
): String {
    val monthNames = MonthNames(
        stringArrayResource(
            id = when {
                monthCase == MonthCase.Nominative -> R.array.feature_title_details_month_names_in_nominative_case
                monthCase == MonthCase.Genitive -> R.array.feature_title_details_month_names_in_genitive_case
                incompleteDate.day != null -> R.array.feature_title_details_month_names_in_genitive_case
                else -> R.array.feature_title_details_month_names_in_nominative_case
            }
        ).toList()
    )

    return DateTimeComponents.Format {
        var addSpace = false
        if (incompleteDate.day != null) {
            dayOfMonth()
            addSpace = true
        }

        if (incompleteDate.month != null) {
            if (addSpace) {
                char(' ')
            }
            monthName(monthNames)
            addSpace = true
        }

        if (incompleteDate.year != null) {
            if (addSpace) {
                char(' ')
            }
            year()
        }
    }.format {
        dayOfMonth = incompleteDate.day
        monthNumber = incompleteDate.month
        year = incompleteDate.year
    }
}

@Composable
private fun animeRatingDescription(rating: AnimeRating) = when (rating) {
    AnimeRating.G -> R.string.feature_title_details_anime_rating_g_description
    AnimeRating.PG -> R.string.feature_title_details_anime_rating_pg_description
    AnimeRating.PG13 -> R.string.feature_title_details_anime_rating_pg13_description
    AnimeRating.R -> R.string.feature_title_details_anime_rating_r_description
    AnimeRating.RPlus -> R.string.feature_title_details_anime_rating_r_plus_description
    AnimeRating.RX -> R.string.feature_title_details_anime_rating_rx_description
    AnimeRating.None -> null
}?.let { stringResource(it) } ?: ""

private enum class MonthCase { Nominative, Genitive, Auto }