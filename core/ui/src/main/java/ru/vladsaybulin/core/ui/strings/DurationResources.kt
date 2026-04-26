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

package ru.vladsaybulin.core.ui.strings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.model.search.Duration

@Composable
fun durationString(duration: Duration) = stringResource(id = durationStringId(duration))

fun durationStringId(duration: Duration) = when (duration) {
    Duration.S -> R.string.core_ui_duration_s
    Duration.D -> R.string.core_ui_duration_d
    Duration.F -> R.string.core_ui_duration_f
}