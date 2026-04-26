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