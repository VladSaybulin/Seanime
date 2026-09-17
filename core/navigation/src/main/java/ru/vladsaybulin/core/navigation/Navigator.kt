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

class Navigator(val state: NavigationState) {

    fun navigateToTopLevel(key: SeanimeNavKey) {
        if (key == state.currentTopLevelKey) {
            clearSubStack()
        } else {
            state.topLevelStack.apply {
                remove(key)
                add(key)
            }
        }
    }

    fun navigateTo(key: SeanimeNavKey) {
        state.currentSubStack.add(key)
    }

    fun back() {
        if (state.currentSubStack.size > 1) {
            state.currentSubStack.removeAt(state.currentSubStack.lastIndex)
        } else if (state.topLevelStack.size > 1) {
            state.topLevelStack.removeAt(state.topLevelStack.lastIndex)
        }
    }

    fun clearSubStack() = state.currentSubStack.run {
        if (size > 1) {
            clear()
            add(state.currentTopLevelKey)
        }
    }
}