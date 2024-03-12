package ru.vladsaybulin.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.model.UserRateStatus

@Composable
@ReadOnlyComposable
fun userRateStatusIcon(userRateStatus: UserRateStatus) = when (userRateStatus) {
    UserRateStatus.Planned -> ShikimoriIcons.Add
    UserRateStatus.Watching -> ShikimoriIcons.Visibility
    UserRateStatus.Rewatching -> ShikimoriIcons.Replay
    UserRateStatus.Completed -> ShikimoriIcons.Done
    UserRateStatus.Dropped -> ShikimoriIcons.Clear
    UserRateStatus.OnHold -> ShikimoriIcons.Pause
    else -> null
}