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

package ru.vladsaybulin.core.ui.filters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ru.vladsaybulin.model.search.FilterOption

enum class OptionValue {
    Selected, Unselected, Excluded
}

class FilterOptionState<T>(
    val option: FilterOption<T>,
    initialStateValue: OptionValue = OptionValue.Unselected
) {
    var value by mutableStateOf(initialStateValue)

    fun onClick() {
        value = when (value) {
            OptionValue.Selected, OptionValue.Excluded -> OptionValue.Unselected
            OptionValue.Unselected -> OptionValue.Selected
        }
    }

    fun onLongClick() {
        value = when (value) {
            OptionValue.Excluded -> OptionValue.Unselected
            OptionValue.Unselected,
            OptionValue.Selected -> OptionValue.Excluded
        }
    }
}