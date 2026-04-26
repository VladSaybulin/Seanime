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

package ru.vladsaybulin.feature.userrate

import androidx.activity.compose.BackHandler
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastSumBy
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import ru.vladsaybulin.core.designsystem.components.ShikimoriModalBottomSheet
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.UserRateStatusButton
import ru.vladsaybulin.core.ui.UserRateStatusButtonDefaults
import ru.vladsaybulin.core.ui.score.ScoreInput
import ru.vladsaybulin.core.ui2.strings.compose.ProvideTitleStringsByType
import ru.vladsaybulin.feature.userrate.CounterState.Companion.UNLIMITED_LIMIT
import ru.vladsaybulin.feature.userrate.ProgressCounterType.Chapters
import ru.vladsaybulin.feature.userrate.ProgressCounterType.Episodes
import ru.vladsaybulin.feature.userrate.ProgressCounterType.Volumes
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.EditableUserRate
import ru.vladsaybulin.model.userrate.UserRate
import ru.vladsaybulin.model.userrate.UserRateStatus
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRateBottomSheet(
    editableUserRate: EditableUserRate,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    viewModel: UserRateViewModel = hiltViewModel()
) {
    val userRateState = editableUserRate.asState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val closeSheet = {
        scope.launch {
            sheetState.hide()
        }.invokeOnCompletion { onDismissRequest() }
    }

    ProvideTitleStringsByType(editableUserRate.titleType) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            modifier = modifier,
            content = {
                UserRateContent(
                    state = userRateState,
                    onSave = {
                        viewModel.save(editableUserRate.userRate.id, userRateState.toUserRateValues())
                        closeSheet()
                    },
                    onDelete = {
                        viewModel.delete(editableUserRate.userRate.id)
                        closeSheet()
                    }
                )
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserRateContent(
    state: UserRateState,
    modifier: Modifier = Modifier,
    onSave: () -> Unit,
    onDelete: () -> Unit
) {
    val (expandedStatusButtons, setExpandedStatusButtons) = remember {
        mutableStateOf(false)
    }

    BackHandler(enabled = expandedStatusButtons) {
        setExpandedStatusButtons(false)
    }

    UserRateLayout(
        availableUseRateStatuses = state.availableStatuses,
        selectedStatus = state.status,
        expandedStatusButtons = expandedStatusButtons,
        onExpandedChange = setExpandedStatusButtons,
        onStatusChanged = state::onStatusChanged,
        modifier = modifier.clipToBounds()
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        ScoreInput(
            score = { state.score },
            onScoreChange = state::onScoreChanged,
            starSize = DpSize(48.dp, 48.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.progressCounterStates.forEach { (type, counterState) ->
                Counter(
                    state = counterState,
                    enabled = state.progressCounterEnabled,
                    label = { Text(counterLabel(type = type)) },
                    limit = if (counterState.limit != UNLIMITED_LIMIT) {
                        {
                            Text(
                                stringResource(
                                    id = R.string.feature_user_rate_counter_default_limit,
                                    counterState.limit
                                )
                            )
                        }
                    } else null
                )
            }

            Counter(
                state = state.rewatchesCounterState,
                label = { Text(stringResource(R.string.counter_label_rewatches)) },
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
        UserRateTextField(
            text = state.text,
            onTextChange = state::onTextChanger,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onSave,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(0.96f)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(id = R.string.save))
        }
        TextButton(
            onClick = onDelete,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(id = R.string.delete),
                color = SeanimeTheme.colorScheme.error
            )
        }

        val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        Spacer(modifier = Modifier.height(bottomPadding))
    }
}

@Composable
private fun UserRateTextField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = text,
        onValueChange = onTextChange,
        textStyle = SeanimeTheme.typography.bodyLarge.copy(
            color = SeanimeTheme.colorScheme.onSurface
        ),
        minLines = TextMinLines,
        cursorBrush = SolidColor(SeanimeTheme.colorScheme.primary),
        modifier = modifier,
    ) { innerTextField ->
        if (text.isEmpty()) {
            Text(
                text = stringResource(id = R.string.text_placeholder),
                style = SeanimeTheme.typography.bodyLarge.copy(
                    color = SeanimeTheme.colorScheme.onSurfaceVariant
                )
            )
        }
        innerTextField()
    }
}

@Composable
private fun UserRateLayout(
    availableUseRateStatuses: List<UserRateStatus>,
    selectedStatus: UserRateStatus,
    expandedStatusButtons: Boolean,
    modifier: Modifier = Modifier,
    onExpandedChange: (Boolean) -> Unit,
    onStatusChanged: (UserRateStatus) -> Unit,
    inputsContent: @Composable ColumnScope.() -> Unit,
) {
    val animatedExpandable by animateFloatAsState(
        targetValue = if (expandedStatusButtons) 1f else 0f,
        label = "ExpandedStatusButtons",
        animationSpec = tween(durationMillis = 300)
    )

    SubcomposeLayout(
        modifier = modifier.clipToBounds()
    ) { constraints ->
        val collapsed = animatedExpandable == 0f

        val buttonsConstraints = constraints.copy(minWidth = 0)
        val buttonsPlaceables = if (collapsed) {
            subcompose(UserRateLayoutSlotId.SelectedButton) {
                SelectedStatusButton(
                    targetStatus = selectedStatus,
                    onClick = { onExpandedChange(true) },
                )
            }
        } else {
            subcompose(UserRateLayoutSlotId.AllButtons) {
                AllStatusButtons(
                    availableStatuses = availableUseRateStatuses,
                    selectedStatus = selectedStatus,
                    onClick = {
                        if (selectedStatus != it) {
                            onStatusChanged(it)
                        }
                        onExpandedChange(false)
                    }
                )
            }
        }.map { it.measure(buttonsConstraints) }
        val buttonsHeight = buttonsPlaceables.fastSumBy { it.height }

        val selectedButtonHeight: Int
        var startSelectedButtonY = 0
        if (!collapsed) {
            val index = availableUseRateStatuses.indexOf(selectedStatus)
            for (i in 0..<index) {
                startSelectedButtonY += buttonsPlaceables[i].height
            }
            selectedButtonHeight = buttonsPlaceables[index].height
        } else {
            selectedButtonHeight = buttonsPlaceables.first().height
        }

        val inputsConstraints = constraints.copy(minHeight = 0)
        val inputsPlaceable = subcompose(slotId = UserRateLayoutSlotId.Inputs) {
            Surface(color = SeanimeTheme.colorScheme.surfaceContainerLow) {
                Column(
                    modifier = Modifier.alpha(1 - animatedExpandable),
                    content = inputsContent
                )
            }
        }.first().measure(inputsConstraints)

        val endHeight = max(buttonsHeight, inputsPlaceable.height) + selectedButtonHeight
        layout(constraints.maxWidth, endHeight) {
            var buttonY = lerp(-startSelectedButtonY, 0, animatedExpandable)
            buttonsPlaceables.forEach {
                it.placeRelative(0, buttonY)
                buttonY += it.height
            }
            inputsPlaceable.placeRelative(
                0,
                lerp(selectedButtonHeight, buttonsHeight, animatedExpandable)
            )
        }
    }
}

@Composable
private fun AllStatusButtons(
    availableStatuses: List<UserRateStatus>,
    selectedStatus: UserRateStatus,
    onClick: (UserRateStatus) -> Unit,
) {
    availableStatuses.forEach {
        UserRateStatusButton(
            userRateStatus = it,
            onClick = { onClick(it) },
            colors = if (it == selectedStatus) {
                UserRateStatusButtonDefaults.userRateStatusButtonColors(userRateStatus = it)
            } else UserRateStatusButtonDefaults.userRateStatusButtonColors(),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun SelectedStatusButton(
    targetStatus: UserRateStatus,
    onClick: () -> Unit,
) {
    AnimatedContent(
        targetState = targetStatus,
        label = "SelectedStatusButton",
        modifier = Modifier.padding(horizontal = 16.dp)
    ) { status ->
        UserRateStatusButton(
            userRateStatus = status,
            onClick = onClick,
            colors = UserRateStatusButtonDefaults.userRateStatusButtonColors(
                userRateStatus = status
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun counterLabel(type: ProgressCounterType) = stringResource(
    id = when (type) {
        Episodes -> R.string.counter_label_episodes
        Chapters -> R.string.counter_label_chapters
        Volumes -> R.string.counter_label_volumes
    }
)

@Composable
@Preview(wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE)
fun AnimeUserRateContentPreview() {

    val state = EditableUserRate(
        userRate = UserRate(
            id = 1,
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now(),
            status = UserRateStatus.Planned,
            score = 7,
            episodes = 0,
            chapters = 0,
            volumes = 0,
            rewatches = 0,
            text = ""
        ),
        titleType = EntryType.Anime,
        entryStatus = EntryStatus.Released,
        maxEpisodes = 14,
        maxChapters = -1,
        maxVolumes = -1
    ).asState()

    SeanimeTheme(darkTheme = true) {
        UserRateContent(
            state = state,
            onSave = { },
            onDelete = { }
        )
    }
}

@Composable
@Preview(wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE)
fun MangaUserRateContentPreview() {

    val state = EditableUserRate(
        userRate = UserRate(
            id = 2,
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now(),
            status = UserRateStatus.Watching,
            score = 0,
            episodes = 0,
            chapters = 1121,
            volumes = 0,
            rewatches = 0,
            text = ""
        ),
        titleType = EntryType.Manga,
        entryStatus = EntryStatus.Ongoing,
        maxEpisodes = -1,
        maxChapters = 0,
        maxVolumes = 0
    ).asState()

    SeanimeTheme(darkTheme = true) {
        UserRateContent(
            state = state,
            onSave = { },
            onDelete = { }
        )
    }
}

private enum class UserRateLayoutSlotId {
    SelectedButton, AllButtons, Inputs
}

private const val TextMinLines = 7
