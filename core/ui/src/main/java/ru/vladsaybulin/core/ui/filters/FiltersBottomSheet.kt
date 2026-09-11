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

package ru.vladsaybulin.core.ui.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.IntState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import kotlinx.coroutines.launch
import ru.vladsaybulin.core.designsystem.components.ShikimoriFilterChip
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.core.ui.score.ScoreInput
import ru.vladsaybulin.core.ui.strings.durationString
import ru.vladsaybulin.core.ui.strings.seasonFilterString
import ru.vladsaybulin.core.ui2.score.Score
import ru.vladsaybulin.core.ui2.score.ScoreFormat
import ru.vladsaybulin.core.ui2.score.StarsRowInput
import ru.vladsaybulin.core.ui2.strings.compose.ProvideTitleStringsByType
import ru.vladsaybulin.core.ui2.strings.compose.asString
import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.search.FilterOption
import ru.vladsaybulin.model.search.FilterType
import ru.vladsaybulin.model.search.Filters
import ru.vladsaybulin.model.search.TimePeriodAiring

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersBottomSheet(
    filtersState: FiltersState,
    onDismissRequest: () -> Unit,
    onApplyFilters: (AppliedFilters) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val closeSheet: () -> Unit = {
        coroutineScope.launch {
            sheetState.hide()
        }.invokeOnCompletion { onDismissRequest() }
    }

    ModalBottomSheet(
        onDismissRequest = {
            onApplyFilters(filtersState.getAppliedFilters())
            onDismissRequest()
        },
        sheetState = sheetState,
        contentWindowInsets = { WindowInsets.statusBars },
        content = {
            FiltersContent(
                filtersState = filtersState,
                onDismissRequest = closeSheet
            )
        }
    )
}

@Composable
fun FiltersContent(
    filtersState: FiltersState,
    onDismissRequest: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            var addDivider = false

            val mayBeDivider: LazyListScope.() -> Unit = {
                if (addDivider) {
                    item { HorizontalDivider() }
                } else {
                    addDivider = true
                }
            }

            if (!filtersState.animeKindOptions.isNullOrEmpty()) {
                mayBeDivider()
                filterHeader(headerTextId = R.string.core_ui_filter_header_kind)
                regularFilter(
                    options = filtersState.animeKindOptions,
                    label = {
                        Text(text = it.value.asString())
                    }
                )
            }


            if (!filtersState.mangaKindOptions.isNullOrEmpty()) {
                mayBeDivider()
                filterHeader(headerTextId = R.string.core_ui_filter_header_kind)
                regularFilter(
                    options = filtersState.mangaKindOptions,
                    label = {
                        Text(text = it.value.asString())
                    }
                )
            }

            if (!filtersState.statusOptions.isNullOrEmpty()) {
                mayBeDivider()
                filterHeader(headerTextId = R.string.core_ui_filter_header_status)
                regularFilter(
                    options = filtersState.statusOptions,
                    label = {
                        Text(text = it.value.asString())
                    }
                )
            }


            if (!filtersState.myListStatusOptions.isNullOrEmpty()) {
                mayBeDivider()
                filterHeader(headerTextId = R.string.core_ui_filter_header_my_list_status)
                regularFilter(
                    options = filtersState.myListStatusOptions,
                    label = {
                        Text(text = it.value.asString())
                    }
                )
            }

            if (!filtersState.durationOptions.isNullOrEmpty()) {
                mayBeDivider()
                filterHeader(headerTextId = R.string.core_ui_filter_header_duration)
                regularFilter(
                    options = filtersState.durationOptions,
                    label = {
                        Text(text = durationString(it.value))
                    }
                )
            }

            if (!filtersState.timePeriodAiringOptions.isNullOrEmpty()) {
                mayBeDivider()
                filterHeader(headerTextId = R.string.core_ui_filter_header_season)
                seasonFilter(
                    options = filtersState.timePeriodAiringOptions,
                    customOptions = filtersState.customTimePeriodAiringOptions,
                    onNewOptionClick = { },
                    onOptionDeleteClick = { },
                )
            }

            if (!filtersState.ratingOptions.isNullOrEmpty()) {
                mayBeDivider()
                filterHeader(headerTextId = R.string.core_ui_filter_header_season)
                regularFilter(
                    options = filtersState.ratingOptions,
                    label = {
                        Text(text = it.value.asString())
                    }
                )
            }

            mayBeDivider()
            filterHeader(headerTextId = R.string.core_ui_filter_header_score)
            scoreFilter(
                selected = filtersState.selectedMinScoreState,
                onScoreChange = filtersState::changeMinScore
            )

            if (!filtersState.demographicOptions.isNullOrEmpty()) {
                mayBeDivider()
                filterHeader(headerTextId = R.string.core_ui_filter_header_demographic)
                regularFilter(
                    options = filtersState.demographicOptions,
                    label = {
                        Text(text = it.value.russianName ?: it.value.englishName)
                    }
                )
            }

            if (!filtersState.themeOptions.isNullOrEmpty()) {
                mayBeDivider()
                filterHeader(headerTextId = R.string.core_ui_filter_header_themes)
                regularFilter(
                    options = filtersState.themeOptions,
                    label = {
                        Text(text = it.value.russianName ?: it.value.englishName)
                    }
                )
            }

            if (!filtersState.genresOptions.isNullOrEmpty()) {
                mayBeDivider()
                filterHeader(headerTextId = R.string.core_ui_filter_header_genres)
                regularFilter(
                    options = filtersState.genresOptions,
                    label = {
                        Text(text = it.value.russianName ?: it.value.englishName)
                    }
                )
            }
        }

        TextButton(
            onClick = {
                filtersState.cancelChanges()
                onDismissRequest()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(text = stringResource(id = R.string.core_ui_cancel))
        }

        TextButton(
            onClick = { filtersState.resetAll() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.core_ui_reset),
                color = SeanimeTheme.colorScheme.error
            )
        }
    }
}

fun LazyListScope.scoreFilter(selected: IntState, onScoreChange: (Int) -> Unit) {
    item {
        Score(
            score = selected.intValue.toFloat(),
            format = ScoreFormat.Integer,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            leading = {
                StarsRowInput(
                    score = selected.intValue,
                    onChanged = onScoreChange,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
fun LazyListScope.seasonFilter(
    options: FilterOptionStates<TimePeriodAiring>,
    customOptions: SnapshotStateList<FilterOptionState<TimePeriodAiring>>,
    onNewOptionClick: () -> Unit,
    onOptionDeleteClick: (Int) -> Unit
) {
    item {
        Column(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                options.fastForEach { optionState ->
                    ShikimoriFilterChip(
                        selected = optionState.value != OptionValue.Unselected,
                        onClick = optionState::onClick,
                        onLongClick = optionState::onLongClick,
                        label = {
                            Text(text = seasonFilterString(timePeriodAiringFilter = optionState.option.value))
                        },
                        leadingIcon = optionState.value.optionStateValueIcon()
                    )
                }
            }
            Text(
                text = stringResource(id = R.string.core_ui_filter_custom_season_options),
                style = SeanimeTheme.typography.labelMedium,
                color = LocalContentColor.current.copy(alpha = 0.7f),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                customOptions.forEach { optionState ->
                    ShikimoriFilterChip(
                        selected = optionState.value != OptionValue.Unselected,
                        onClick = optionState::onClick,
                        onLongClick = optionState::onLongClick,
                        label = {
                            Text(text = seasonFilterString(timePeriodAiringFilter = optionState.option.value))
                        },
                        leadingIcon = optionState.value.optionStateValueIcon(),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    onOptionDeleteClick(requireNotNull(optionState.option.value.id))
                                }
                            ) {
                                Icon(imageVector = SeanimeIcons.Clear, contentDescription = null)
                            }
                        }
                    )
                }

                ShikimoriFilterChip(
                    selected = false,
                    onClick = onNewOptionClick,
                    label = {
                        Text(text = stringResource(id = R.string.core_ui_filter_add_custom_season))
                    },
                    leadingIcon = {
                        Icon(imageVector = SeanimeIcons.Add, contentDescription = null)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
fun <T> LazyListScope.regularFilter(
    options: FilterOptionStates<T>,
    label: @Composable (FilterOption<T>) -> Unit
) {
    item {
        FlowRow(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            options.fastForEach { optionState ->
                ShikimoriFilterChip(
                    selected = optionState.value != OptionValue.Unselected,
                    onClick = optionState::onClick,
                    onLongClick = optionState::onLongClick,
                    label = { label(optionState.option) },
                    leadingIcon = optionState.value.optionStateValueIcon()
                )
            }
        }
    }
}

fun LazyListScope.filterHeader(headerTextId: Int) {
    item {
        Text(
            text = stringResource(id = headerTextId),
            style = SeanimeTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

fun OptionValue.optionStateValueIcon(): (@Composable () -> Unit)? =
    when (this) {
        OptionValue.Unselected -> null
        OptionValue.Selected -> {
            { SelectedIcon() }
        }

        OptionValue.Excluded -> {
            { ExcludedIcon() }
        }
    }

@Composable
fun SelectedIcon() {
    Icon(imageVector = SeanimeIcons.Done, contentDescription = null)
}

@Composable
fun ExcludedIcon() {
    Icon(imageVector = SeanimeIcons.Remove, contentDescription = null)
}

@Composable
@Preview
fun FiltersContentPreview() {
    ProvideTitleStringsByType(titleType = EntryType.Anime) {
        SeanimeTheme {
            Surface {
                FiltersContent(
                    filtersState = rememberFiltersState(
                        filters = Filters(
                            animeKindOptions = AnimeKind.entries
                                .filter { it != AnimeKind.None }
                                .map { FilterOption(it, it.serializedName) },
                            statusOptions = EntryStatus.entries
                                .filter { it != EntryStatus.None }
                                .map { FilterOption(it, it.serializedName) },
                        ),
                        appliedFilters = mapOf(FilterType.Score to mapOf("7" to OptionValue.Selected))
                    ),
                    onDismissRequest = { }
                )
            }
        }
    }
}
