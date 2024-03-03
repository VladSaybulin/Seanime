package ru.vladsaybulin.core.ui.colors

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.model.UserRateStatus

@Composable
fun entryStatusColor(entryStatus: EntryStatus): Color = when (entryStatus) {
    EntryStatus.Ongoing -> ShikimoriTheme.entryStatusColors.ongoing
    EntryStatus.Released -> ShikimoriTheme.entryStatusColors.released
    EntryStatus.Anons -> ShikimoriTheme.entryStatusColors.anons
    EntryStatus.Paused -> ShikimoriTheme.entryStatusColors.paused
    EntryStatus.Discontinued -> ShikimoriTheme.entryStatusColors.discontinued
    EntryStatus.None -> Color.Unspecified
}

@Composable
fun rateStatusColor(userRateStatus: UserRateStatus) =
    when (userRateStatus) {
        UserRateStatus.Planned -> ShikimoriTheme.userRateColors.planned
        UserRateStatus.Watching, UserRateStatus.Rewatching -> ShikimoriTheme.userRateColors.watching
        UserRateStatus.Completed -> ShikimoriTheme.userRateColors.completed
        UserRateStatus.Dropped -> ShikimoriTheme.userRateColors.dropped
        UserRateStatus.OnHold -> ShikimoriTheme.userRateColors.onHold
        else -> Color.Unspecified
    }

@Composable
fun onRateStatusColor(userRateStatus: UserRateStatus) = when (userRateStatus) {
    UserRateStatus.Planned -> ShikimoriTheme.userRateColors.onPlanned
    UserRateStatus.Watching, UserRateStatus.Rewatching -> ShikimoriTheme.userRateColors.onWatching
    UserRateStatus.Completed -> ShikimoriTheme.userRateColors.onCompleted
    UserRateStatus.Dropped -> ShikimoriTheme.userRateColors.onDropped
    UserRateStatus.OnHold -> ShikimoriTheme.userRateColors.onOnHold
    else -> Color.Unspecified
}

@Composable
fun rateStatusContainer(userRateStatus: UserRateStatus) =
    when (userRateStatus) {
        UserRateStatus.Planned -> ShikimoriTheme.userRateColors.plannedContainer
        UserRateStatus.Watching, UserRateStatus.Rewatching -> ShikimoriTheme.userRateColors.watchingContainer
        UserRateStatus.Completed -> ShikimoriTheme.userRateColors.completedContainer
        UserRateStatus.Dropped -> ShikimoriTheme.userRateColors.droppedContainer
        UserRateStatus.OnHold -> ShikimoriTheme.userRateColors.onHoldContainer
        else -> MaterialTheme.colorScheme.surface
    }

@Composable
fun onRateStatusContainer(userRateStatus: UserRateStatus) =
    when (userRateStatus) {
        UserRateStatus.Planned -> ShikimoriTheme.userRateColors.onPlannedContainer
        UserRateStatus.Watching, UserRateStatus.Rewatching -> ShikimoriTheme.userRateColors.onWatchingContainer
        UserRateStatus.Completed -> ShikimoriTheme.userRateColors.onCompletedContainer
        UserRateStatus.Dropped -> ShikimoriTheme.userRateColors.onDroppedContainer
        UserRateStatus.OnHold -> ShikimoriTheme.userRateColors.onOnHoldContainer
        else -> MaterialTheme.colorScheme.onSurface
    }