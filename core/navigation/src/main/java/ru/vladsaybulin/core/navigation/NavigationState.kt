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

package ru.vladsaybulin.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.serialization.NavBackStackSerializer
import androidx.navigation3.runtime.serialization.NavKeySerializer

@Composable
fun rememberNavigationState(
    topLevelKeys: List<SeanimeNavKey>,
    startKey: SeanimeNavKey
): NavigationState {
    val topLevelStack = rememberSeanimeNavBackStack(startKey)

    val subStacks = topLevelKeys.associateWith { key ->
        rememberSeanimeNavBackStack(key)
    }

    return NavigationState(
        topLevelStack = topLevelStack,
        subStacks = subStacks
    )
}

class NavigationState(
    val topLevelStack: NavBackStack<SeanimeNavKey>,
    val subStacks: Map<SeanimeNavKey, NavBackStack<SeanimeNavKey>>
) {
    val currentTopLevelKey: SeanimeNavKey by derivedStateOf {topLevelStack.last() }

    val topLevelKeys
        get() = subStacks.keys

    val currentSubStack: NavBackStack<SeanimeNavKey>
        get() = subStacks[currentTopLevelKey] ?: error("No sub stack for $currentTopLevelKey")

    val currentKey by derivedStateOf { currentSubStack.last() }

    val navigator = Navigator(this)
}

@Composable
private fun rememberSeanimeNavBackStack(vararg keys: SeanimeNavKey): NavBackStack<SeanimeNavKey> {
    return rememberSerializable(
        serializer = NavBackStackSerializer(elementSerializer = NavKeySerializer())
    ) {
        NavBackStack(*keys)
    }
}
