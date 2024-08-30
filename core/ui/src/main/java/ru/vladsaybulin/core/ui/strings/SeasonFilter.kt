package ru.vladsaybulin.core.ui.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.model.search.SeasonOfYear
import ru.vladsaybulin.model.search.TimePeriodAiring

@Composable
@ReadOnlyComposable
fun seasonFilterString(timePeriodAiringFilter: TimePeriodAiring) = when (timePeriodAiringFilter) {
    is TimePeriodAiring.Decade -> stringResource(
        id = R.string.core_ui_season_filter_decade,
        timePeriodAiringFilter.decade
    )

    is TimePeriodAiring.Year -> stringResource(
        id = R.string.core_ui_season_filter_year,
        timePeriodAiringFilter.year
    )

    is TimePeriodAiring.YearRange -> stringResource(
        id = R.string.core_ui_season_filter_summer_range_year,
        timePeriodAiringFilter.begin,
        timePeriodAiringFilter.end
    )

    is TimePeriodAiring.Season -> seasonYearString(timePeriodAiringYear = timePeriodAiringFilter)
}

@Composable
@ReadOnlyComposable
private fun seasonYearString(timePeriodAiringYear: TimePeriodAiring.Season) = when (timePeriodAiringYear.seasonOfYear) {
    SeasonOfYear.Summer -> stringResource(
        id = R.string.core_ui_season_filter_summer_year,
        timePeriodAiringYear.year
    )

    SeasonOfYear.Spring -> stringResource(
        id = R.string.core_ui_season_filter_spring_year,
        timePeriodAiringYear.year
    )

    SeasonOfYear.Winter -> stringResource(
        id = R.string.core_ui_season_filter_winter_year,
        timePeriodAiringYear.year
    )

    SeasonOfYear.Fall -> stringResource(
        id = R.string.core_ui_season_filter_fall_year,
        timePeriodAiringYear.year
    )
}