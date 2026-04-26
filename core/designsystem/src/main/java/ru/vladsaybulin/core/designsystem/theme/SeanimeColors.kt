/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vladsaybulin.core.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.userrate.UserRateStatus

data class SeanimeColors(
    val userRateStatusColors: Map<UserRateStatus, UserRateStatusColors>,
    val entryStatusColors: Map<EntryStatus, Color>,
    val posterScrim: Color,
    val onPosterScrim: Color,
)

data class UserRateStatusColors(
    val color: Color,
    val onColor: Color,
    val container: Color,
    val onContainer: Color
) {
    companion object {
        val Unspecified = UserRateStatusColors(
            color = Color.Unspecified,
            onColor = Color.Unspecified,
            container = Color.Unspecified,
            onContainer = Color.Unspecified
        )
    }
}

fun lightSeanimeColors(
    posterScrim: Color,
    onPosterScrim: Color,
    anons: Color = Orange40,
    ongoing: Color = Blue40,
    released: Color = Green40,
    paused: Color = Violet40,
    discontinued: Color = Red40,
    planned: Color = Orange40,
    onPlanned: Color = White,
    plannedContainer: Color = Orange90,
    onPlannedContainer: Color = Orange10,
    watching: Color = Blue40,
    onWatching: Color = White,
    watchingContainer: Color = Blue90,
    onWatchingContainer: Color = Blue10,
    completed: Color = Green40,
    onCompleted: Color = White,
    completedContainer: Color = Green95,
    onCompletedContainer: Color = Green10,
    onHold: Color = Violet40,
    onOnHold: Color = White,
    onHoldContainer: Color = Violet90,
    onOnHoldContainer: Color = Violet10,
    dropped: Color = Red40,
    onDropped: Color = White,
    droppedContainer: Color = Red90,
    onDroppedContainer: Color = Red10
) = SeanimeColors(
    posterScrim = posterScrim,
    onPosterScrim = onPosterScrim,
    anons = anons,
    ongoing = ongoing,
    released = released,
    paused = paused,
    discontinued = discontinued,
    planned = planned,
    onPlanned = onPlanned,
    plannedContainer = plannedContainer,
    onPlannedContainer = onPlannedContainer,
    watching = watching,
    onWatching = onWatching,
    watchingContainer = watchingContainer,
    onWatchingContainer = onWatchingContainer,
    completed = completed,
    onCompleted = onCompleted,
    completedContainer = completedContainer,
    onCompletedContainer = onCompletedContainer,
    onHold = onHold,
    onOnHold = onOnHold,
    onHoldContainer = onHoldContainer,
    onOnHoldContainer = onOnHoldContainer,
    dropped = dropped,
    onDropped = onDropped,
    droppedContainer = droppedContainer,
    onDroppedContainer = onDroppedContainer,
)

fun darkSeanimeColors(
    posterScrim: Color,
    onPosterScrim: Color,
    planned: Color = Orange80,
    onPlanned: Color = Orange20,
    plannedContainer: Color = Orange30,
    onPlannedContainer: Color = Orange90,
    watching: Color = Blue80,
    onWatching: Color = Blue20,
    watchingContainer: Color = Blue30,
    onWatchingContainer: Color = Blue90,
    completed: Color = Green80,
    onCompleted: Color = Green20,
    completedContainer: Color = Green30,
    onCompletedContainer: Color = Green90,
    onHold: Color = Violet80,
    onOnHold: Color = Violet20,
    onHoldContainer: Color = Violet30,
    onOnHoldContainer: Color = Violet90,
    dropped: Color = Red80,
    onDropped: Color = Red20,
    droppedContainer: Color = Red30,
    onDroppedContainer: Color = Red90,
    anons: Color = Orange80,
    ongoing: Color = Blue80,
    released: Color = Green80,
    paused: Color = Violet80,
    discontinued: Color = Red80,
) = SeanimeColors(
    posterScrim = posterScrim,
    onPosterScrim = onPosterScrim,
    anons = anons,
    ongoing = ongoing,
    released = released,
    paused = paused,
    discontinued = discontinued,
    planned = planned,
    onPlanned = onPlanned,
    plannedContainer = plannedContainer,
    onPlannedContainer = onPlannedContainer,
    watching = watching,
    onWatching = onWatching,
    watchingContainer = watchingContainer,
    onWatchingContainer = onWatchingContainer,
    completed = completed,
    onCompleted = onCompleted,
    completedContainer = completedContainer,
    onCompletedContainer = onCompletedContainer,
    onHold = onHold,
    onOnHold = onOnHold,
    onHoldContainer = onHoldContainer,
    onOnHoldContainer = onOnHoldContainer,
    dropped = dropped,
    onDropped = onDropped,
    droppedContainer = droppedContainer,
    onDroppedContainer = onDroppedContainer,
)

fun SeanimeColors(
    posterScrim: Color,
    onPosterScrim: Color,
    anons: Color = Color.Unspecified,
    ongoing: Color = Color.Unspecified,
    released: Color = Color.Unspecified,
    paused: Color = Color.Unspecified,
    discontinued: Color = Color.Unspecified,
    planned: Color = Color.Unspecified,
    onPlanned: Color = Color.Unspecified,
    plannedContainer: Color = Color.Unspecified,
    onPlannedContainer: Color = Color.Unspecified,
    watching: Color = Color.Unspecified,
    onWatching: Color = Color.Unspecified,
    watchingContainer: Color = Color.Unspecified,
    onWatchingContainer: Color = Color.Unspecified,
    completed: Color = Color.Unspecified,
    onCompleted: Color = Color.Unspecified,
    completedContainer: Color = Color.Unspecified,
    onCompletedContainer: Color = Color.Unspecified,
    onHold: Color = Color.Unspecified,
    onOnHold: Color = Color.Unspecified,
    onHoldContainer: Color = Color.Unspecified,
    onOnHoldContainer: Color = Color.Unspecified,
    dropped: Color = Color.Unspecified,
    onDropped: Color = Color.Unspecified,
    droppedContainer: Color = Color.Unspecified,
    onDroppedContainer: Color = Color.Unspecified
): SeanimeColors {
    val watchingColors = UserRateStatusColors(
        color = watching,
        onColor = onWatching,
        container = watchingContainer,
        onContainer = onWatchingContainer
    )
    return SeanimeColors(
        posterScrim = posterScrim,
        onPosterScrim = onPosterScrim,
        userRateStatusColors = mapOf(
            UserRateStatus.Planned to UserRateStatusColors(
                color = planned,
                onColor = onPlanned,
                container = plannedContainer,
                onContainer = onPlannedContainer
            ),
            UserRateStatus.Watching to watchingColors,
            UserRateStatus.Rewatching to watchingColors,
            UserRateStatus.Completed to UserRateStatusColors(
                color = completed,
                onColor = onCompleted,
                container = completedContainer,
                onContainer = onCompletedContainer
            ),
            UserRateStatus.OnHold to UserRateStatusColors(
                color = onHold,
                onColor = onOnHold,
                container = onHoldContainer,
                onContainer = onOnHoldContainer
            ),
            UserRateStatus.Dropped to UserRateStatusColors(
                color = dropped,
                onColor = onDropped,
                container = droppedContainer,
                onContainer = onDroppedContainer
            )
        ),
        entryStatusColors = mapOf(
            EntryStatus.Anons to anons,
            EntryStatus.Ongoing to ongoing,
            EntryStatus.Released to released,
            EntryStatus.Paused to paused,
            EntryStatus.Discontinued to discontinued
        )
    )
}

val LocalSeanimeColors = staticCompositionLocalOf<SeanimeColors> {
    error("SeanimeColors not provided")
}

operator fun SeanimeColors.get(status: UserRateStatus) =
    userRateStatusColors[status] ?: UserRateStatusColors.Unspecified

operator fun SeanimeColors.get(status: EntryStatus) =
    entryStatusColors[status] ?: Color.Unspecified