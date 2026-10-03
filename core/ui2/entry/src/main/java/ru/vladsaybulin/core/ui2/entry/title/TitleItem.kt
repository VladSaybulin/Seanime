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

package ru.vladsaybulin.core.ui2.entry.title

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import ru.vladsaybulin.core.ui2.entry.EntryGridItem
import ru.vladsaybulin.core.ui2.entry.EntryItemColors
import ru.vladsaybulin.core.ui2.entry.EntryItemDefaults
import ru.vladsaybulin.model.title.Title
import ru.vladsaybulin.model.userrate.UserRateStatus

@Composable
fun TitleGridItem(
    title: Title,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    colors: EntryItemColors = EntryItemDefaults.basedOnUserRateStatusColors(userRateStatus),
    nameStyle: TextStyle = EntryItemDefaults.GridNameStyle,
    infoPadding: PaddingValues = EntryItemDefaults.GridPadding,
    badgeSize: Dp = EntryItemDefaults.GridBadgeSize,
    shape: Shape = EntryItemDefaults.GridShape,
    additionalContent: (@Composable () -> Unit)? = null,
) {
    EntryGridItem(
        entry = title,
        onClick = onClick,
        modifier = modifier,
        userRateStatus = userRateStatus,
        colors = colors,
        nameStyle = nameStyle,
        infoPadding = infoPadding,
        badgeSize = badgeSize,
        shape = shape,
        additionalContent = additionalContent
    )
}

@Composable
fun TitleListItem(
    title: Title,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    colors: EntryItemColors = EntryItemDefaults.basedOnUserRateStatusColors(userRateStatus),
    nameStyle: TextStyle = EntryItemDefaults.ListNameStyle,
    infoPadding: PaddingValues = EntryItemDefaults.ListPadding,
    badgeSize: Dp = EntryItemDefaults.ListBadgeSize,
    shape: Shape = EntryItemDefaults.ListShape,
    additionalContent: (@Composable () -> Unit)? = null,
) {
    EntryGridItem(
        name = title.name,
        russianName = title.nameRu,
        poster = title.poster,
        onClick = onClick,
        modifier = modifier,
        userRateStatus = userRateStatus,
        colors = colors,
        nameStyle = nameStyle,
        infoPadding = infoPadding,
        badgeSize = badgeSize,
        shape = shape,
        additionalContent = additionalContent
    )
}

@Composable
fun TitleCarouselItem(
    title: Title,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    colors: EntryItemColors = EntryItemDefaults.basedOnUserRateStatusColors(userRateStatus),
    nameStyle: TextStyle = EntryItemDefaults.CarouselNameStyle,
    infoPadding: PaddingValues = EntryItemDefaults.CarouselPadding,
    badgeSize: Dp = EntryItemDefaults.CarouselBadgeSize,
    shape: Shape = EntryItemDefaults.CarouselShape,
    additionalContent: (@Composable () -> Unit)? = null,
) {
    EntryGridItem(
        name = title.name,
        russianName = title.nameRu,
        poster = title.poster,
        onClick = onClick,
        modifier = modifier,
        userRateStatus = userRateStatus,
        colors = colors,
        nameStyle = nameStyle,
        infoPadding = infoPadding,
        badgeSize = badgeSize,
        shape = shape,
        additionalContent = additionalContent
    )
}