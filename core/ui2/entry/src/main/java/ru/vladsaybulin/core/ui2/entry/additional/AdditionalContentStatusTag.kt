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

package ru.vladsaybulin.core.ui2.entry.additional

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vladsaybulin.core.designsystem.components.SeanimeTag
import ru.vladsaybulin.core.designsystem.components.TagDefaults
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.designsystem.theme.get
import ru.vladsaybulin.core.ui2.strings.compose.asString
import ru.vladsaybulin.model.common.EntryStatus

@Composable
internal fun AdditionalContentStatusTag(
    status: EntryStatus,
    modifier: Modifier = Modifier
) {
    if (status != EntryStatus.None) {
        val color = SeanimeTheme.seanimeColors[status]
        SeanimeTag(
            border = TagDefaults.border(color = color),
            contentColor = color,
            modifier = modifier
        ) {
            Text(status.asString())
        }
    }
}