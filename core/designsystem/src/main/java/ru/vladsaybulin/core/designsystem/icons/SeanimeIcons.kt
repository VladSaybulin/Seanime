package ru.vladsaybulin.core.designsystem.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.vector.ImageVector
import ru.vladsaybulin.model.userrate.UserRateStatus

object SeanimeIcons {
    val AccountCircle = Icons.Filled.AccountCircle
    val Construction = Icons.Filled.Construction
    val Logout = Icons.AutoMirrored.Filled.Logout
    val OutlinedInfo = Icons.Outlined.Info
    val Edit = Icons.Default.Edit
    val ArrowDownward = Icons.Default.ArrowDownward
    val ArrowUpward = Icons.Default.ArrowUpward
    val Tune = Icons.Default.Tune
    val History = Icons.Default.History
    val Home = Icons.Filled.Home
    val OutlinedHome = Icons.Outlined.Home
    val Bookmark = Icons.Filled.Bookmark
    val OutlinedBookmark = Icons.Outlined.BookmarkBorder
    val ArrowDropDown = Icons.Filled.ArrowDropDown
    val Remove = Icons.Default.Remove
    val Search = Icons.Default.Search
    val PlayArrow = Icons.Default.PlayArrow
    val KeyboardArrowDown = Icons.Default.KeyboardArrowDown
    val AccessTime = Icons.Default.AccessTime
    val Groups: ImageVector = Icons.Filled.Groups
    val Person: ImageVector = Icons.Default.Person
    val CalendarToday = Icons.Default.CalendarToday
    val Add = Icons.Default.Add
    val Visibility = Icons.Filled.Visibility
    val Replay = Icons.Filled.Replay
    val Done = Icons.Filled.Done
    val Pause = Icons.Filled.Pause
    val Clear = Icons.Filled.Clear
    val Star = Icons.Rounded.Star
    val StarOutline = Icons.Rounded.StarOutline
    val ArrowBack = Icons.AutoMirrored.Default.ArrowBack
    val Book = Icons.Default.MenuBook
    val Tv = Icons.Default.Tv
    val Schedule = Icons.Default.Schedule
    val ArrowForwardIos = Icons.AutoMirrored.Filled.ArrowForwardIos
}

@Composable
@ReadOnlyComposable
fun userRateStatusIcon(userRateStatus: UserRateStatus) = when (userRateStatus) {
    UserRateStatus.Planned -> SeanimeIcons.Add
    UserRateStatus.Watching -> SeanimeIcons.Visibility
    UserRateStatus.Rewatching -> SeanimeIcons.Replay
    UserRateStatus.Completed -> SeanimeIcons.Done
    UserRateStatus.Dropped -> SeanimeIcons.Clear
    UserRateStatus.OnHold -> SeanimeIcons.Pause
    else -> error("UserRateStatus.None not supported")
}