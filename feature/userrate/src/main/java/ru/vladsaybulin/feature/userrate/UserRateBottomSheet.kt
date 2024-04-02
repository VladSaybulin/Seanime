package ru.vladsaybulin.feature.userrate

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastSumBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.feature.userrate.components.Counter
import ru.vladsaybulin.feature.userrate.components.ScoreRow
import ru.vladsaybulin.feature.userrate.components.UserRateStatusButton
import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.UserRate
import ru.vladsaybulin.model.UserRateStatus
import kotlin.math.max
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRateBottomSheet(
    viewModel: UserRateViewModel,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit
) {

    val state = viewModel.setup.collectAsUserRateState() ?: return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val closeSheet = {
        scope.launch {
            sheetState.hide()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        UserRateContent(
            state = state,
            onSave = {
                state.userRateValues?.let { viewModel.save(it) }
                closeSheet()
            },
            onDelete = {
                viewModel.delete()
                closeSheet()
            }
        )
    }
}

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

    UserRateLayout(
        availableUseRateStatuses = state.availableStatuses,
        selectedStatus = state.status,
        expandedStatusButtons = expandedStatusButtons,
        onExpandedChange = setExpandedStatusButtons,
        onStatusChanged = state::setStatus,
        modifier = modifier.background(ShikimoriTheme.colorScheme.surface)
    ) {
        Column {
            Spacer(modifier = Modifier.height(8.dp))
            ScoreRow(
                state = state.scoreState,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                state.episodesState?.let {
                    Counter(state = it)
                }
                state.chaptersState?.let {
                    Counter(state = it)
                }
                state.volumesState?.let {
                    Counter(state = it)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Counter(
                state = state.rewatchesState,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            UserRateTextField(
                text = state.text,
                onTextChange = { state.text = it },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            HorizontalDivider()
            Button(
                onClick = onSave,
                enabled = state.enabledSaveButton,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.save))
            }

            TextButton(
                onClick = onDelete,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.delete))
            }
        }
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
        textStyle = ShikimoriTheme.typography.bodyLarge.copy(
            color = ShikimoriTheme.colorScheme.onSurface
        ),
        minLines = TextMinLines,
        modifier = modifier,
    ) { innerTextField ->
        if (text.isEmpty()) {
            Text(
                text = stringResource(id = R.string.text_placeholder),
                style = ShikimoriTheme.typography.bodyLarge.copy(
                    color = ShikimoriTheme.colorScheme.onSurfaceVariant
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
    inputsContent: @Composable BoxScope.() -> Unit,
) {
    val animatedExpandable by animateFloatAsState(
        targetValue = if (expandedStatusButtons) 1f else 0f,
        label = "ExpandedStatusButtons"
    )

    SubcomposeLayout(
        modifier = modifier.clipToBounds()
    ) { constraints ->
        val buttonsConstraints = constraints.copy(minWidth = 0)
        val buttonsPlaceables = if (animatedExpandable == 0f) {
            subcompose(UserRateLayoutSlotId.SelectedButton) {
                SelectedStatusButton(
                    targetStatus = selectedStatus,
                    onClick = { onExpandedChange(true) }
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

        var selectedButtonY = 0
        if (animatedExpandable > 0f) {
            val index = availableUseRateStatuses.indexOf(selectedStatus)
            for (i in 0..<index) {
                selectedButtonY += buttonsPlaceables[i].height
            }
        }

        val inputsConstraints = constraints.copy(minHeight = 0)
        val inputsPlaceable = subcompose(slotId = UserRateLayoutSlotId.Inputs) {
            Box(
                modifier = Modifier.alpha(1 - animatedExpandable),
                content = inputsContent
            )
        }.first().measure(inputsConstraints)

        val initialY = -(animatedExpandable * selectedButtonY).roundToInt()

        val endHeight = max(buttonsHeight, inputsPlaceable.height)
        layout(constraints.maxWidth, endHeight) {
            var y = initialY
            buttonsPlaceables.forEach {
                it.placeRelative(0, y)
                y += it.height
            }
            inputsPlaceable.placeRelative(0, y)
        }
    }
}

@Composable
private fun AllStatusButtons(
    availableStatuses: List<UserRateStatus>,
    selectedStatus: UserRateStatus,
    onClick: (UserRateStatus) -> Unit,
) {
    Column {
        availableStatuses.forEach {
            UserRateStatusButton(
                userRateStatus = it,
                selected = it == selectedStatus,
                onClick = { onClick(it) },
            )
        }
    }
}

@Composable
private fun SelectedStatusButton(
    targetStatus: UserRateStatus,
    onClick: () -> Unit,
) {
    AnimatedContent(
        targetState = targetStatus,
        label = "SelectedStatusButton"
    ) { status ->
        UserRateStatusButton(
            userRateStatus = status,
            selected = true,
            onClick = onClick,
        )
    }
}

@Composable
@Preview
fun UserRateContentPreview() {

    val state = MutableStateFlow(
        UserRateSetup.Success(
            userRate = UserRate(
                id = 1,
                createdAt = Clock.System.now(),
                updatedAt = Clock.System.now(),
                status = UserRateStatus.Planned,
                score = 7,
                episodes = 14,
                chapters = 0,
                volumes = 0,
                rewatches = 0,
                text = ""
            ),
            context = UserRateEditorContext(
                entryType = EntryType.Anime,
                entryStatus = EntryStatus.Ongoing,
                episodesLimit = Limit.Limited(17),
                chaptersLimit = null,
                volumesLimit = null
            ),
            enabledAutocorrect = false
        )
    ).collectAsUserRateState()

    ShikimoriTheme {
        UserRateContent(
            state = state!!,
            onSave = { },
            onDelete = {  }
        )
    }
}

private enum class UserRateLayoutSlotId {
    SelectedButton, AllButtons, Inputs
}

private const val TextMinLines = 7
