package ru.vladsaybulin.core.ui.colors

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.userrate.UserRateStatus

@Composable
fun entryStatusColor(entryStatus: EntryStatus): Color = when (entryStatus) {
    EntryStatus.Ongoing -> SeanimeTheme.entryStatusColors.ongoing
    EntryStatus.Released -> SeanimeTheme.entryStatusColors.released
    EntryStatus.Anons -> SeanimeTheme.entryStatusColors.anons
    EntryStatus.Paused -> SeanimeTheme.entryStatusColors.paused
    EntryStatus.Discontinued -> SeanimeTheme.entryStatusColors.discontinued
    EntryStatus.None -> Color.Unspecified
}

@Composable
fun userRateStatusColor(userRateStatus: UserRateStatus) =
    when (userRateStatus) {
        UserRateStatus.Planned -> SeanimeTheme.userRateColors.planned
        UserRateStatus.Watching, UserRateStatus.Rewatching -> SeanimeTheme.userRateColors.watching
        UserRateStatus.Completed -> SeanimeTheme.userRateColors.completed
        UserRateStatus.Dropped -> SeanimeTheme.userRateColors.dropped
        UserRateStatus.OnHold -> SeanimeTheme.userRateColors.onHold
        else -> Color.Unspecified
    }

@Composable
fun onUserRateStatusColor(userRateStatus: UserRateStatus) = when (userRateStatus) {
    UserRateStatus.Planned -> SeanimeTheme.userRateColors.onPlanned
    UserRateStatus.Watching, UserRateStatus.Rewatching -> SeanimeTheme.userRateColors.onWatching
    UserRateStatus.Completed -> SeanimeTheme.userRateColors.onCompleted
    UserRateStatus.Dropped -> SeanimeTheme.userRateColors.onDropped
    UserRateStatus.OnHold -> SeanimeTheme.userRateColors.onOnHold
    else -> Color.Unspecified
}

@Composable
fun userRateStatusContainerColor(userRateStatus: UserRateStatus) =
    when (userRateStatus) {
        UserRateStatus.Planned -> SeanimeTheme.userRateColors.plannedContainer
        UserRateStatus.Watching, UserRateStatus.Rewatching -> SeanimeTheme.userRateColors.watchingContainer
        UserRateStatus.Completed -> SeanimeTheme.userRateColors.completedContainer
        UserRateStatus.Dropped -> SeanimeTheme.userRateColors.droppedContainer
        UserRateStatus.OnHold -> SeanimeTheme.userRateColors.onHoldContainer
        else -> MaterialTheme.colorScheme.surface
    }

@Composable
fun onUserRateStatusContainerColor(userRateStatus: UserRateStatus) =
    when (userRateStatus) {
        UserRateStatus.Planned -> SeanimeTheme.userRateColors.onPlannedContainer
        UserRateStatus.Watching, UserRateStatus.Rewatching -> SeanimeTheme.userRateColors.onWatchingContainer
        UserRateStatus.Completed -> SeanimeTheme.userRateColors.onCompletedContainer
        UserRateStatus.Dropped -> SeanimeTheme.userRateColors.onDroppedContainer
        UserRateStatus.OnHold -> SeanimeTheme.userRateColors.onOnHoldContainer
        else -> MaterialTheme.colorScheme.onSurface
    }