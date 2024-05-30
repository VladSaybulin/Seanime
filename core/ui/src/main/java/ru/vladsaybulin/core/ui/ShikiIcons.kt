package ru.vladsaybulin.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.model.userrate.UserRateStatus

@Composable
@ReadOnlyComposable
fun userRateStatusIcon(userRateStatus: UserRateStatus) = when (userRateStatus) {
    UserRateStatus.Planned -> SeanimeIcons.Add
    UserRateStatus.Watching -> SeanimeIcons.Visibility
    UserRateStatus.Rewatching -> SeanimeIcons.Replay
    UserRateStatus.Completed -> SeanimeIcons.Done
    UserRateStatus.Dropped -> SeanimeIcons.Clear
    UserRateStatus.OnHold -> SeanimeIcons.Pause
    else -> null
}

@Composable
@ReadOnlyComposable
fun notNoneUserRateStatusIcon(userRateStatus: UserRateStatus) = when (userRateStatus) {
    UserRateStatus.Planned -> SeanimeIcons.Add
    UserRateStatus.Watching -> SeanimeIcons.Visibility
    UserRateStatus.Rewatching -> SeanimeIcons.Replay
    UserRateStatus.Completed -> SeanimeIcons.Done
    UserRateStatus.Dropped -> SeanimeIcons.Clear
    UserRateStatus.OnHold -> SeanimeIcons.Pause
    else -> throw IllegalArgumentException("UserRateStatus is None")
}