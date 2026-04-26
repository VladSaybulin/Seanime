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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.components.SeanimeInformation
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme

@Composable
fun ErrorMessage(
    throwable: Throwable,
    modifier: Modifier = Modifier
) {
    SeanimeInformation(
        modifier = modifier,
        header = { Text(text = stringResource(id = ERROR_HEADER_ID)) },
        description = {
            var showDetails by remember { mutableStateOf(false) }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(id = ERROR_DESCRIPTION_ID))

                AnimatedVisibility(visible = showDetails) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .border(
                                width = 1.dp,
                                color = SeanimeTheme.colorScheme.outlineVariant,
                                shape = SeanimeTheme.shapes.medium
                            )
                            .background(
                                color = SeanimeTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                shape = SeanimeTheme.shapes.medium
                            )
                            .padding(8.dp)
                    ) {
                        Text(
                            text = checkNotNull(throwable.message),
                            color = LocalContentColor.current,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                if (throwable.message != null) {
                    TextButton(
                        onClick = { showDetails = !showDetails },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(
                                id = if (showDetails) {
                                    R.string.core_ui_error_hide_details
                                } else R.string.core_ui_error_show_details
                            )
                        )
                    }
                }
            }
        }
    )
}

@Composable
@Preview
fun ErrorMessagePreview() {
    SeanimeTheme {
        Surface {
            ErrorMessage(throwable = RuntimeException("Test"))
        }
    }
}

@Composable
@Preview
fun ErrorMessageWithoutMessagePreview() {
    SeanimeTheme {
        Surface {
            ErrorMessage(throwable = RuntimeException())
        }
    }
}

private val ERROR_HEADER_ID = R.string.core_ui_error_title
private val ERROR_DESCRIPTION_ID = R.string.core_ui_error_description

@Composable
fun ErrorMessageColumn(
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit = { ErrorMessageColumnDefaults.HeaderText() },
    description: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        val primaryColor = SeanimeTheme.colorScheme.primary
        val headerTextStyle = SeanimeTheme.typography.headlineMedium

        ProvideTextStyle(value = headerTextStyle.copy(color = primaryColor)) {
            header()
        }

        if (description != null) {
            ProvideTextStyle(value = SeanimeTheme.typography.bodyMedium) {
                description()
            }
        }

        if (action != null) {
            action()
        }
    }
}

@Composable
fun ErrorMessageRow(
    header: @Composable () -> Unit,
    description: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            val primaryColor = SeanimeTheme.colorScheme.primary
            val headerTextStyle = SeanimeTheme.typography.headlineMedium

            ProvideTextStyle(value = headerTextStyle.copy(color = primaryColor)) {
                header()
            }

            ProvideTextStyle(value = SeanimeTheme.typography.bodyMedium) {
                description()
            }
        }

        if (action != null) {
            action()
        }
    }
}

object ErrorMessageColumnDefaults {

    @Composable
    fun HeaderText() {
        Text(text = stringResource(id = R.string.core_ui_error))
    }
}


@Composable
fun FullScreenErrorMessage(
    throwable: Throwable,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

    }
}

private val ErrorMessageRowHeight = 64.dp