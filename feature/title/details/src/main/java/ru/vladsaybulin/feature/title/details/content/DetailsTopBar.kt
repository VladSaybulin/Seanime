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

package ru.vladsaybulin.feature.title.details.content

import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailsTopBar(
    visibleTopBar: Boolean,
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = TopAppBarDefaults.pinnedScrollBehavior(),
) {
    val defaultTopAppBarColors = TopAppBarDefaults.topAppBarColors()

    //TopAppBar doesn't animate titleContentColor
    val titleContentColor by animateColorAsState(
        targetValue = if (visibleTopBar) {
            defaultTopAppBarColors.titleContentColor
        } else {
            defaultTopAppBarColors.titleContentColor.copy(alpha = 0f)
        },
        label = "titleContentColor"
    )

    TopAppBar(
        title = {
            Text(text = title, maxLines = 1)
        },
        navigationIcon = {
            BackIconButton(
                showScrim = !visibleTopBar,
                onClick = onBackClick
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (visibleTopBar) {
                defaultTopAppBarColors.containerColor
            } else {
                defaultTopAppBarColors.containerColor.copy(alpha = 0f)
            },
            scrolledContainerColor = if (visibleTopBar) {
                defaultTopAppBarColors.scrolledContainerColor
            } else {
                defaultTopAppBarColors.scrolledContainerColor.copy(alpha = 0f)
            },
            titleContentColor = titleContentColor
        ),
        modifier = modifier,
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun BackIconButton(
    showScrim: Boolean,
    onClick: () -> Unit,
) {
    val containerColor by animateColorAsState(
        targetValue = if (showScrim) {
            SeanimeTheme.colorScheme.surface.copy(alpha = NavigationButtonContainerAlpha)
        } else {
            SeanimeTheme.colorScheme.surface.copy(alpha = 0f)
        },
        label = "BackIconButtonScrim"
    )

    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(containerColor = containerColor)
    ) {
        Icon(
            imageVector = SeanimeIcons.ArrowBack,
            contentDescription = null
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun DetailsTopBarPreview() {
    SeanimeTheme {
        Surface(color = SeanimeTheme.colorScheme.primary) {
            CompositionLocalProvider(value = LocalContentColor provides SeanimeTheme.colorScheme.onSurface) {
                DetailsTopBar(
                    visibleTopBar = true,
                    title = "Entry details top bar",
                    onBackClick = { }
                )
            }
        }
    }
}

private const val NavigationButtonContainerAlpha = 0.7f