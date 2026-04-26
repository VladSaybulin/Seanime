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

package ru.vladsaybulin.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme

@Composable
fun SeanimeInformation(
    header: @Composable () -> Unit,
    description: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    headerTextStyle: TextStyle = SeanimeInformationDefaults.headerTextStyle,
    descriptionTextStyle: TextStyle = SeanimeInformationDefaults.descriptionTextStyle
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        icon?.invoke()

        ProvideTextStyle(value = headerTextStyle, content = header)

        ProvideTextStyle(value = descriptionTextStyle, content = description)

        action?.invoke()
    }
}

object SeanimeInformationDefaults {
    val headerTextStyle: TextStyle
        @Composable get() = SeanimeTheme.typography.headlineMedium.copy(color = DefaultHeaderColor)

    val descriptionTextStyle: TextStyle
        @Composable get() = SeanimeTheme.typography.bodyMedium
}

private val DefaultHeaderColor
    @Composable get() = SeanimeTheme.colorScheme.secondary