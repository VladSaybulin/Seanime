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

package ru.vladsaybulin.seanime.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import ru.vladsaybulin.core.navigation.NavigationState
import ru.vladsaybulin.core.navigation.rememberNavigationState
import ru.vladsaybulin.seanime.navigation.TopLevelDestination
import ru.vladsaybulin.seanime.navigation.TopLevelDestination.HOME

@Composable
fun rememberSeanimeAppState(
    windowSizeClass: WindowSizeClass,
    navigationState: NavigationState = rememberNavigationState(
        topLevelKeys = TopLevelDestination.entries.map(TopLevelDestination::navKey),
        startKey = HOME.navKey
    )
) : SeanimeAppState {
    return remember(
        windowSizeClass,
        navigationState
    ) {
        SeanimeAppState(
            navState = navigationState,
            windowSizeClass = windowSizeClass
        )
    }
}

@Stable
class SeanimeAppState(
    val windowSizeClass: WindowSizeClass,
    val navState: NavigationState,
) {
    val shouldShowBottomBar: Boolean
        @Composable get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowNavRail: Boolean
        @Composable get() = !shouldShowBottomBar
}