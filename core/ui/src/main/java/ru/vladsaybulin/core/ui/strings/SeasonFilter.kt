package ru.vladsaybulin.core.ui.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.model.search.Season
import ru.vladsaybulin.model.search.SeasonFilter

@Composable
@ReadOnlyComposable
fun seasonFilterString(seasonFilter: SeasonFilter) = when (seasonFilter) {
    is SeasonFilter.Decade -> stringResource(
        id = R.string.core_ui_season_filter_decade,
        seasonFilter.decade
    )

    is SeasonFilter.Year -> stringResource(
        id = R.string.core_ui_season_filter_year,
        seasonFilter.year
    )

    is SeasonFilter.YearRange -> stringResource(
        id = R.string.core_ui_season_filter_summer_range_year,
        seasonFilter.begin,
        seasonFilter.end
    )

    is SeasonFilter.SeasonYear -> seasonYearString(seasonYear = seasonFilter)
}

@Composable
@ReadOnlyComposable
private fun seasonYearString(seasonYear: SeasonFilter.SeasonYear) = when (seasonYear.season) {
    Season.Summer -> stringResource(
        id = R.string.core_ui_season_filter_summer_year,
        seasonYear.year
    )

    Season.Spring -> stringResource(
        id = R.string.core_ui_season_filter_spring_year,
        seasonYear.year
    )

    Season.Winter -> stringResource(
        id = R.string.core_ui_season_filter_winter_year,
        seasonYear.year
    )

    Season.Fall -> stringResource(
        id = R.string.core_ui_season_filter_fall_year,
        seasonYear.year
    )
}