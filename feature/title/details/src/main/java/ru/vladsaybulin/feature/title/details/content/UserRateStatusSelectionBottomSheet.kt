package ru.vladsaybulin.feature.title.details.content

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vladsaybulin.core.ui.UserRateStatusButton
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.UserRateStatus.Completed
import ru.vladsaybulin.model.userrate.UserRateStatus.Dropped
import ru.vladsaybulin.model.userrate.UserRateStatus.OnHold
import ru.vladsaybulin.model.userrate.UserRateStatus.Planned
import ru.vladsaybulin.model.userrate.UserRateStatus.Rewatching
import ru.vladsaybulin.model.userrate.UserRateStatus.Watching

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UserRateStatusSelectionBottomSheet(
    enabledAutocorrect: Boolean,
    entryStatus: EntryStatus,
    onStatusClick: (UserRateStatus) -> Unit,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        modifier = modifier,
        content = {
            buildList {
                add(Planned)
                if (!enabledAutocorrect || entryStatus != EntryStatus.Anons) {
                    add(Watching)
                    add(Rewatching)
                    if (!enabledAutocorrect || entryStatus != EntryStatus.Ongoing) {
                        add(Completed)
                    }
                }
                add(OnHold)
                add(Dropped)
            }.forEach {
                UserRateStatusButton(
                    userRateStatus = it,
                    onClick = { onStatusClick(it) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.navigationBarsPadding())
        })
}