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