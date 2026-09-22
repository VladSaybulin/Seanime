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

package ru.vladsaybulin.feature.rate.editor.impl

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.MultiContentMeasurePolicy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastSumBy
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.icons.userRateStatusIcon
import ru.vladsaybulin.core.designsystem.theme.LocalSeanimeColors
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.designsystem.theme.get
import ru.vladsaybulin.core.domain.rate.Progress
import ru.vladsaybulin.core.ui2.score.Score
import ru.vladsaybulin.core.ui2.score.StarsRowInput
import ru.vladsaybulin.core.ui2.strings.compose.ProvideTitleStringsByType
import ru.vladsaybulin.core.ui2.strings.compose.asString
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateContext
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.UserRateValues

@Composable
fun RateEditorSheet(
    viewModel: RateEditorViewModel,
    onCloseRequest: () -> Unit
) {
    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collect { effect ->
            if (effect == RateEditorViewModel.UiEffect.Finished) {
                onCloseRequest()
            }
        }
    }

    val loadState by viewModel.loadState.collectAsStateWithLifecycle()

    when (val localLoadState = loadState) {
        RateEditorViewModel.LoadState.Loading -> {
            LoadingContent()
        }

        is RateEditorViewModel.LoadState.Success -> {
            EditorContent(
                viewModel = viewModel,
                loadState = localLoadState
            )
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EditorContent(
    viewModel: RateEditorViewModel,
    loadState: RateEditorViewModel.LoadState.Success
) {
    ProvideTitleStringsByType(viewModel.titleType) {
        val state = rememberRateEditorState(
            rateValues = loadState.rateValues,
            context = loadState.context,
            availableStatuses = loadState.availableStatuses,
            titleType = viewModel.titleType,
        )

        LaunchedEffect(viewModel.forceChanges) {
            viewModel.forceChanges.collect { forceChange ->
                when (forceChange) {
                    is RateEditorViewModel.ForceChanges.Status -> state.status = forceChange.status
                    is RateEditorViewModel.ForceChanges.Progression -> state.setProgress(forceChange.progress)
                }
            }
        }

        val uiEffect = viewModel.uiEffect.collectAsStateWithLifecycle(RateEditorViewModel.UiEffect.Idle)

        EditorContent(
            state = state,
            uiEffect = uiEffect.value,
            onStatusChangeTriggered = viewModel::onStatusChangeTriggered,
            onProgressChangeTriggered = viewModel::onProgressChangeTriggered,
            onSaveClick = {
                viewModel.onSaveClick(state.toUserRateValues())
            },
            onDeleteClick = viewModel::onDeleteClick
        )
    }
}

@Composable
private fun EditorContent(
    state: RateEditorState,
    uiEffect: RateEditorViewModel.UiEffect,
    onStatusChangeTriggered: (prevStatus: UserRateStatus, newStatus: UserRateStatus) -> Unit,
    onProgressChangeTriggered: (currentStatus: UserRateStatus, newProgress: Progress) -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var statusChooseMode by remember { mutableStateOf(state.status == UserRateStatus.None) }

    val transition = updateTransition(
        targetState = statusChooseMode,
        label = "StatusChooseTransition"
    )

    EditorLayout(
        statuses = {
            StatusSelection(
                allStatuses = state.allStatuses,
                selectedStatusIndex = state.statusIndex,
                transition = transition,
                onStatusClick = { index ->
                    statusChooseMode = !statusChooseMode
                    if (index != state.statusIndex) {
                        val prevStatus = state.status
                        state.statusIndex = index
                        onStatusChangeTriggered(prevStatus, state.status)
                    }
                }
            )
        },
        other = {
            transition.AnimatedVisibility(
                visible = { !it },
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(OtherInputsSpacing),
                ) {
                    ScoreSelection(
                        score = state.score,
                        onScoreChange = { newScore ->
                            state.score = newScore
                        }
                    )
                    ProgressCounters(
                        episodesState = state.episodes,
                        chaptersState = state.chapters,
                        volumesState = state.volumes,
                        onProgressChangeTriggered = { newProgress ->
                            onProgressChangeTriggered(state.status, newProgress)
                        }
                    )
                    NotesField(
                        text = state.text.value,
                        onTextChange = { newText ->
                            state.text.value = newText
                        }
                    )
                    UserButtons(
                        readUiEffect = { uiEffect },
                        onSaveClick = onSaveClick,
                        onDeleteClick = onDeleteClick
                    )
                }
            }
        }
    )
}

@Composable
private fun StatusSelection(
    allStatuses: List<UserRateStatus>,
    selectedStatusIndex: Int,
    transition: Transition<Boolean>,
    onStatusClick: (Int) -> Unit
) {
    if (transition.currentState || transition.targetState) {
        allStatuses.forEachIndexed { index, status ->
            val selected = index == selectedStatusIndex

            transition.AnimatedVisibility(
                visible = { it || selected },
                enter = expandVertically(expandFrom = Alignment.Top),
                exit = shrinkVertically(shrinkTowards = Alignment.Top)
            ) {
                StatusButton(
                    status = status,
                    selected = selected,
                    onClick = { onStatusClick(index) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    } else {
        AnimatedContent(
            targetState = selectedStatusIndex
        ) { selected ->
            val status = allStatuses.getOrElse(selected) { UserRateStatus.None }

            StatusButton(
                status = status,
                selected = true,
                onClick = { onStatusClick(selected) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun StatusButton(
    status: UserRateStatus,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = if (selected) {
        val colors = LocalSeanimeColors.current[status]
        ButtonDefaults.buttonColors(
            containerColor = colors.color,
            contentColor = colors.onColor
        )
    } else {
        ButtonDefaults.textButtonColors(
            containerColor = Color.Transparent,
            contentColor = SeanimeTheme.colorScheme.onSurface
        )
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .height(ButtonSize)
            .padding(vertical = StatusButtonSpacing / 2, horizontal = 16.dp),
        colors = colors,
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding
    ) {
        Icon(
            imageVector = userRateStatusIcon(status),
            contentDescription = null
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = status.asString(),
            modifier = Modifier.fillMaxWidth(),
            style = SeanimeTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun ScoreSelection(
    score: Int,
    onScoreChange: (Int) -> Unit
) {
    Score(
        score = score.toFloat(),
        modifier = Modifier.padding(horizontal = 16.dp),
        style = SeanimeTheme.typography.displaySmall,
        leading = {
            BoxWithConstraints {
                val starsSize = min(maxWidth / 5, ButtonSize)
                StarsRowInput(
                    score = score,
                    onChanged = onScoreChange,
                    iconSize = starsSize
                )
            }
        },
    )
}

@Composable
private fun ProgressCounters(
    episodesState: CounterState?,
    chaptersState: CounterState?,
    volumesState: CounterState?,
    onProgressChangeTriggered: (Progress) -> Unit
) {
    LaunchedEffect(episodesState?.value, chaptersState?.value, volumesState?.value) {
        Progress(
            episodes = episodesState?.value ?: -1,
            chapters = chaptersState?.value ?: -1,
            volumes = volumesState?.value ?: -1
        ).let(onProgressChangeTriggered)
    }

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DefaultHorizontalPadding),
        horizontalArrangement = Arrangement.spacedBy(OtherInputsSpacing),
        verticalArrangement = Arrangement.spacedBy(OtherInputsSpacing)
    ) {
        episodesState?.let { state ->
            Counter(
                label = { Text(stringResource(R.string.feature_rate_editor_counter_label_episodes)) },
                state = state,
                limit = if (state.isLimited) {
                    { Text(stringResource(R.string.feature_rate_editor_counter_limit, state.limit)) }
                } else null
            )
        }
        chaptersState?.let { state ->
            Counter(
                label = { Text(stringResource(R.string.feature_rate_editor_counter_label_chapters)) },
                state = state,
                limit = if (state.isLimited) {
                    { Text(stringResource(R.string.feature_rate_editor_counter_limit, state.limit)) }
                } else null
            )
        }
        volumesState?.let { state ->
            Counter(
                label = { Text(stringResource(R.string.feature_rate_editor_counter_label_volumes)) },
                state = state,
                limit = if (state.isLimited) {
                    { Text(stringResource(R.string.feature_rate_editor_counter_limit, state.limit)) }
                } else null
            )
        }

        Counter(
            label = { Text(stringResource(R.string.feature_rate_editor_counter_label_rewatches)) },
            state = CounterState(0)
        )
    }
}

@Composable
private fun NotesField(text: String, onTextChange: (String) -> Unit) {
    BasicTextField(
        value = text,
        onValueChange = onTextChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DefaultHorizontalPadding),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(8.dp)
            ) {
                HorizontalDivider(modifier = Modifier.align(Alignment.TopCenter))

                Box(modifier = Modifier.padding(top = 8.dp)) {
                    if (text.isEmpty()) {
                        Text(
                            text = stringResource(R.string.feature_rate_editor_notes_hint),
                            style = SeanimeTheme.typography.bodyLarge,
                            color = SeanimeTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}

@Composable
private fun UserButtons(
    readUiEffect: () -> RateEditorViewModel.UiEffect,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DefaultHorizontalPadding),
        verticalArrangement = Arrangement.spacedBy(OtherInputsSpacing)
    ) {
        val uiEffect = readUiEffect()

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onSaveClick,
                modifier = Modifier
                    .weight(1f)
                    .height(ButtonSize),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SeanimeTheme.colorScheme.primary,
                    contentColor = SeanimeTheme.colorScheme.onPrimary
                ),
                enabled = uiEffect == RateEditorViewModel.UiEffect.Idle
            ) {
                if (uiEffect == RateEditorViewModel.UiEffect.Saving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(InButtonProgressSize),
                        color = SeanimeTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text = stringResource(R.string.feature_rate_editor_save))
                }
            }

            FilledTonalIconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(ButtonSize),
                enabled = uiEffect == RateEditorViewModel.UiEffect.Idle
            ) {
                if (uiEffect == RateEditorViewModel.UiEffect.Deleting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(InButtonProgressSize),
                        color = SeanimeTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = SeanimeIcons.Delete,
                        contentDescription = stringResource(R.string.feature_rate_editor_delete),
                    )
                }
            }
        }
    }
}

@Composable
private fun EditorLayout(
    statuses: @Composable () -> Unit,
    other: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val measurePolicy = remember { EditorMeasurePolicy() }

    Layout(
        contents = listOf(statuses, other),
        measurePolicy = measurePolicy,
        modifier = modifier.animateContentSize()
    )
}

private class EditorMeasurePolicy : MultiContentMeasurePolicy {

    var peekHeight = 0

    override fun MeasureScope.measure(
        measurables: List<List<Measurable>>,
        constraints: Constraints
    ): MeasureResult {
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        //Status buttons

        val buttonPlaceables = measurables[0].map { it.measure(looseConstraints) }

        var totalButtonsHeight = 0
        var maxButtonHeight = 0
        buttonPlaceables.fastForEach {
            totalButtonsHeight += it.height
            maxButtonHeight = maxOf(maxButtonHeight, it.height)
        }


        val otherPaddingPx = OtherInputsSpacing.roundToPx()
        //Other content

        val otherPlaceables = measurables[1].map { it.measure(looseConstraints) }

        val totalOtherHeight = otherPlaceables.fastSumBy { it.height } + otherPaddingPx

        peekHeight = maxOf(maxButtonHeight + totalOtherHeight, peekHeight)
        peekHeight = maxOf(totalButtonsHeight, peekHeight)

        return layout(constraints.maxWidth, peekHeight) {
            var yPosition = 0

            buttonPlaceables.fastForEach { placeable ->
                placeable.placeRelative(0, yPosition)
                yPosition += placeable.height
            }
            yPosition += otherPaddingPx
            otherPlaceables.fastForEach { placeable ->
                placeable.placeRelative(0, yPosition)
                yPosition += placeable.height
            }
        }
    }

}

@Composable
@Preview
fun RateEditorSelectedStatusButtonPreview() {
    SeanimeTheme {
        Surface {
            ProvideTitleStringsByType(EntryType.Anime) {
                StatusButton(
                    status = UserRateStatus.Watching,
                    selected = true,
                    onClick = {}
                )
            }
        }
    }
}

@Composable
@Preview
fun RateEditorUnselectedStatusButtonPreview() {
    SeanimeTheme {
        Surface {
            ProvideTitleStringsByType(EntryType.Anime) {
                StatusButton(
                    status = UserRateStatus.Watching,
                    selected = false,
                    onClick = {}
                )
            }
        }
    }
}

@Composable
@Preview
fun RateEditorContentPreview() {
    SeanimeTheme {
        Surface {
            val state = rememberRateEditorState(
                rateValues = UserRateValues(
                    status = UserRateStatus.None,
                    score = 8,
                    episodes = 5,
                    chapters = 0,
                    volumes = 0,
                    rewatches = 0,
                    text = "This is a sample review text."
                ),
                context = UserRateContext(
                    titleStatus = EntryStatus.Released,
                    maxEpisodes = 12,
                    maxChapters = 0,
                    maxVolumes = 0
                ),
                availableStatuses = UserRateStatus.entries.toList(),
                titleType = EntryType.Anime
            )

            ProvideTitleStringsByType(EntryType.Anime) {
                EditorContent(
                    state = state,
                    uiEffect = RateEditorViewModel.UiEffect.Idle,
                    onStatusChangeTriggered = { _, _ -> },
                    onProgressChangeTriggered = { _, _ ->},
                    onSaveClick = {},
                    onDeleteClick = {}
                )
            }
        }
    }
}

private val InButtonProgressSize = 24.dp
private val ButtonSize = 58.dp
private val StatusButtonSpacing = 8.dp
private val OtherInputsSpacing = 16.dp
private val DefaultHorizontalPadding = 16.dp
