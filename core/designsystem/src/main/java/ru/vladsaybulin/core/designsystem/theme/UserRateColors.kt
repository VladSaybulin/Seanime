package ru.vladsaybulin.core.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class UserRateColors(
    val planned: Color = Color.Unspecified,
    val onPlanned: Color = Color.Unspecified,
    val plannedContainer: Color = Color.Unspecified,
    val onPlannedContainer: Color = Color.Unspecified,
    val watching: Color = Color.Unspecified,
    val onWatching: Color = Color.Unspecified,
    val watchingContainer: Color = Color.Unspecified,
    val onWatchingContainer: Color = Color.Unspecified,
    val completed: Color = Color.Unspecified,
    val onCompleted: Color = Color.Unspecified,
    val completedContainer: Color = Color.Unspecified,
    val onCompletedContainer: Color = Color.Unspecified,
    val onHold: Color = Color.Unspecified,
    val onOnHold: Color = Color.Unspecified,
    val onHoldContainer: Color = Color.Unspecified,
    val onOnHoldContainer: Color = Color.Unspecified,
    val dropped: Color = Color.Unspecified,
    val onDropped: Color = Color.Unspecified,
    val droppedContainer: Color = Color.Unspecified,
    val onDroppedContainer: Color = Color.Unspecified,
)

val LocalUserRateColors = staticCompositionLocalOf<UserRateColors> {
    error("Rate status colors not provided")
}