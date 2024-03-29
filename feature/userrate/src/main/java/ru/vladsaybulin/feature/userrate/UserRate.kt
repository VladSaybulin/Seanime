package ru.vladsaybulin.feature.userrate

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastSumBy
import kotlinx.datetime.Clock
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.feature.userrate.components.Counter
import ru.vladsaybulin.feature.userrate.components.ScoreRow
import ru.vladsaybulin.feature.userrate.components.UserRateStatusButton
import ru.vladsaybulin.model.UserRate
import ru.vladsaybulin.model.UserRateStatus
import kotlin.math.roundToInt

@Composable
fun UserRateBottomSheetContent(
    viewModel: UserRateViewModel,
    modifier: Modifier = Modifier,
    hideBottomSheet: () -> Unit
) {
    val uiState = rememberUserRateUiState(setup = viewModel.requireSetup)

    UserRateBottomSheetContent(
        state = uiState,
        modifier = modifier,
        onSave = {
            viewModel.save(
                status = uiState.status,
                score = uiState.scoreState.selectedScore,
                episodes = uiState.episodesCounterState?.requireCountInt,
                chapters = uiState.chaptersCounterState?.requireCountInt,
                volumes = uiState.volumesCounterState?.requireCountInt,
                text = uiState.text
            )
            hideBottomSheet()
        },
        onDelete = {
            viewModel.delete()
            hideBottomSheet()
        }
    )
}

@Composable
private fun UserRateBottomSheetContent(
    state: UserRateUiState,
    modifier: Modifier = Modifier,
    onSave: () -> Unit,
    onDelete: () -> Unit,
) {
    val selectedStatus = state.status
    var expandedButtons by remember { mutableStateOf(false) }

    val statuses = listOf(
        UserRateStatus.Planned,
        UserRateStatus.Watching,
        UserRateStatus.Rewatching,
        UserRateStatus.Completed,
        UserRateStatus.OnHold,
        UserRateStatus.Dropped
    )

    UserRateStatusLayout(
        expandedButtons = expandedButtons,
        selectedStatusIndex = statuses.indexOf(selectedStatus),
        modifier = modifier,
        statusButtons = {
            statuses.forEach {
                UserRateStatusButton(
                    selected = it == selectedStatus,
                    userRateStatus = it,
                    onClick = {
                        if (expandedButtons) {
                            state.status = it
                            expandedButtons = false
                        } else {
                            expandedButtons = true
                        }
                    }
                )
            }
        }
    ) {
        Surface {
            Column {
                ScoreRow(state = state.scoreState)

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    state.episodesCounterState?.let {
                        Counter(
                            state = it,
                            label = { Text(text = stringResource(id = R.string.counter_label_episodes)) }
                        )
                    }

                    state.chaptersCounterState?.let {
                        Counter(
                            state = it,
                            label = { Text(text = stringResource(id = R.string.counter_label_chapters)) }
                        )
                    }

                    state.volumesCounterState?.let {
                        Counter(
                            state = it,
                            label = { Text(text = stringResource(id = R.string.counter_label_volumes)) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider()

                BasicTextField(
                    value = "",
                    onValueChange = {},
                    decorationBox = {
                        Text(
                            "Пара слов о тайтле",
                            color = LocalContentColor.current.copy(alpha = 0.5f)
                        )
                        it()
                    },
                    minLines = 10
                )

                HorizontalDivider()

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onSave,
                    enabled = state.enabledSaveButton,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.save))
                }

                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(contentColor = ShikimoriTheme.colorScheme.error),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = stringResource(id = R.string.delete))
                }
            }
        }
    }
}

@Composable
fun UserRateStatusLayout(
    expandedButtons: Boolean,
    selectedStatusIndex: Int,
    modifier: Modifier = Modifier,
    statusButtons: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val animateProgress by animateFloatAsState(targetValue = if (expandedButtons) 1f else 0f)

    SubcomposeLayout(modifier = modifier) { constraints ->
        val constraintsForButtons = constraints.copy(minHeight = 0)
        val buttonMeasurables = subcompose(UserRateStatusSlots.Buttons, statusButtons)
        val buttonPlaceables = if (animateProgress == 0f) {
            listOf(buttonMeasurables[selectedStatusIndex].measure(constraintsForButtons))
        } else {
            buttonMeasurables.fastMap { it.measure(constraintsForButtons) }
        }
        val buttonsHeight = buttonPlaceables.fastSumBy { it.height }

        val constraintsForContent = constraints.copy(minWidth = 0, minHeight = buttonsHeight)
        val contentMeasurables = subcompose(UserRateStatusSlots.Content, content)
        val contentPlaceables = contentMeasurables.map {
            it.measure(constraintsForContent)
        }

        val width = constraints.maxWidth
        val height = contentPlaceables.maxOf { it.height }

        val initialY = if (animateProgress > 0f) {
            var selectedButtonOffset = 0f
            for (i in 0..<selectedStatusIndex) {
                selectedButtonOffset += buttonPlaceables[i].height
            }
            -selectedButtonOffset + animateProgress * selectedButtonOffset
        } else 0f

        val selectedButtonHeight = if (animateProgress > 0f) {
            buttonPlaceables[selectedStatusIndex].height
        } else buttonPlaceables.first().height

        layout(width, height) {
            var y = initialY
            buttonPlaceables.fastForEachIndexed { index, placeable ->
                placeable.place(
                    x = 0,
                    y = y.roundToInt(),
                )
                y += placeable.height
            }

            contentPlaceables.fastForEach { placeable ->
                placeable.placeRelative(x = 0, y = (selectedButtonHeight + (height - selectedButtonHeight) * animateProgress).roundToInt())
            }
        }
    }
}

enum class UserRateStatusSlots {
    Buttons, Content
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun UserRateBottomSheetContentPreview() {

    val setup = UserRateSetup.AnimeUserRate(
        userRate = UserRate(
            id = 0,
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now(),
            status = UserRateStatus.Watching,
            score = 8,
            episodes = 102,
            chapters = 0,
            volumes = 0,
            text = ""
        ),
        maxEpisodes = 156,
        released = false
    )

    val uiState = rememberUserRateUiState(setup = setup)

    ShikimoriTheme {
        ModalBottomSheet(onDismissRequest = {  }) {
            UserRateBottomSheetContent(
                state = uiState,
                onSave = { },
                onDelete = { }
            )
        }
    }

}
