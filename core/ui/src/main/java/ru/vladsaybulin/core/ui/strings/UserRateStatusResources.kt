package ru.vladsaybulin.core.ui.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.model.UserRateStatus

@Composable
@ReadOnlyComposable
fun userRateStatusString(userRateStatus: UserRateStatus) =
    userRateStatusString(userRateStatus, LocalTargetStringsEntry.current)

@Composable
@ReadOnlyComposable
fun userRateStatusString(userRateStatus: UserRateStatus, target: TargetStringsEntry) =
    userRateStatusStringId(userRateStatus, target)
        ?.let { stringResource(id = it) }
        ?: userRateStatus.name

@Composable
@ReadOnlyComposable
fun userRateStatusStringId(userRateStatus: UserRateStatus) = userRateStatusStringId(
    userRateStatus = userRateStatus,
    target = LocalTargetStringsEntry.current
)

fun userRateStatusStringId(userRateStatus: UserRateStatus, target: TargetStringsEntry) = when (target) {
    TargetStringsEntry.Anime -> animeUserRateStatusStringId(userRateStatus)
    TargetStringsEntry.Manga -> mangaUserRateStatusStringId(userRateStatus)
}

fun animeUserRateStatusStringId(userRateStatus: UserRateStatus) = when (userRateStatus) {
    UserRateStatus.Planned -> R.string.user_rate_status_planned
    UserRateStatus.Watching -> R.string.user_rate_status_watching
    UserRateStatus.Rewatching -> R.string.user_rate_status_rewatching
    UserRateStatus.Completed -> R.string.user_rate_status_completed
    UserRateStatus.OnHold -> R.string.user_rate_status_on_hold
    UserRateStatus.Dropped -> R.string.user_rate_status_dropped
    UserRateStatus.None -> null
}

fun mangaUserRateStatusStringId(userRateStatus: UserRateStatus) = when (userRateStatus) {
    UserRateStatus.Planned -> R.string.user_rate_status_planned
    UserRateStatus.Watching -> R.string.user_rate_status_reading
    UserRateStatus.Rewatching -> R.string.user_rate_status_rereading
    UserRateStatus.Completed -> R.string.user_rate_status_read
    UserRateStatus.OnHold -> R.string.user_rate_status_on_hold
    UserRateStatus.Dropped -> R.string.user_rate_status_dropped
    UserRateStatus.None -> null
}
