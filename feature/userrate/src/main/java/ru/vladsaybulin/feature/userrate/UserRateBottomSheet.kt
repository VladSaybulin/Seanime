package ru.vladsaybulin.feature.userrate

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastSumBy
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.feature.userrate.components.Counter
import ru.vladsaybulin.feature.userrate.components.ScoreRow
import ru.vladsaybulin.feature.userrate.components.UserRateStatusButton
import ru.vladsaybulin.feature.userrate.components.UserRateStatusButtonDefaults
import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.UserRate
import ru.vladsaybulin.model.UserRateStatus
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRateBottomSheet(
    viewModel: UserRateViewModel,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit
) {
    val setupState = viewModel.setup.collectAsStateWithLifecycle()

    val state = rememberUserRateState(setup = setupState.value) ?: return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val closeSheet = {
        scope.launch {
            sheetState.hide()
        }.invokeOnCompletion { onDismissRequest() }
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
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
        modifier = modifier.clipToBounds()
    ) {
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
                Counter(
                    state = it,
                    label = { Text(stringResource(R.string.counter_label_episodes)) }
                )
            }
            state.chaptersState?.let {
                Counter(
                    state = it,
                    label = { Text(stringResource(R.string.counter_label_chapters)) }
                )
            }
            state.volumesState?.let {
                Counter(
                    state = it,
                    label = { Text(stringResource(R.string.counter_label_volumes)) }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Counter(
            state = state.rewatchesState,
            modifier = Modifier.padding(horizontal = 16.dp),
            label = { Text(stringResource(R.string.counter_label_rewatches)) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
        UserRateTextField(
            text = state.text,
            onTextChange = { state.text = it },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onSave,
            enabled = state.enabledSaveButton,
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
                color = ShikimoriTheme.colorScheme.error
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
            Surface {
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
@Preview(wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE)
fun UserRateContentPreview() {

    val state = checkNotNull(
        rememberUserRateState(
            setup = UserRateSetup.Edit(
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
                    entryStatus = EntryStatus.Released,
                    episodesLimit = Limit.Limited(17),
                    chaptersLimit = null,
                    volumesLimit = null
                ),
                enabledAutocorrect = true
            )
        )
    )


    ShikimoriTheme(darkTheme = true) {
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
