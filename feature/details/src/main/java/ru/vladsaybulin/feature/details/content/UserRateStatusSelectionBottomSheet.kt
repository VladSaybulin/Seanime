package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vladsaybulin.core.designsystem.components.ShikimoriModalBottomSheet
import ru.vladsaybulin.core.ui.UserRateStatusButton
import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.UserRateStatus
import ru.vladsaybulin.model.UserRateStatus.Completed
import ru.vladsaybulin.model.UserRateStatus.Dropped
import ru.vladsaybulin.model.UserRateStatus.OnHold
import ru.vladsaybulin.model.UserRateStatus.Planned
import ru.vladsaybulin.model.UserRateStatus.Rewatching
import ru.vladsaybulin.model.UserRateStatus.Watching

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UserRateStatusSelectionBottomSheet(
    enabledAutocorrect: Boolean,
    entryStatus: EntryStatus,
    entryType: EntryType,
    onStatusClick: (UserRateStatus) -> Unit,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
) {
    ShikimoriModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
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
                entryType = entryType,
                onClick = { onStatusClick(it) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}