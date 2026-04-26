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

package ru.vladsaybulin.core.ui2.strings.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui2.strings.AnimeStrings
import ru.vladsaybulin.core.ui2.strings.DependsOnTitleStrings
import ru.vladsaybulin.core.ui2.strings.MangaStrings
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateStatus

@Composable
@ReadOnlyComposable
fun EntryStatus.asString() =
    stringResource(id = LocalTitleStrings.current.titleStatusId(this))

@Composable
@ReadOnlyComposable
fun EntryStatus.asStringOrNull(): String? {
    if (this == EntryStatus.None) return null
    return asString()
}

@Composable
@ReadOnlyComposable
fun UserRateStatus.asString() =
    stringResource(id = LocalTitleStrings.current.userStatusId(this))

@Composable
@ReadOnlyComposable
fun UserRateStatus.asStringOrNull(): String? {
    if (this == UserRateStatus.None) return null
    return asString()
}

val LocalTitleStrings = staticCompositionLocalOf<DependsOnTitleStrings> {
    error("LocalTitleStrings not provided")
}

@Composable
fun ProvideTitleStringsByType(
    titleType: EntryType,
    content: @Composable () -> Unit
) {
    val strings: DependsOnTitleStrings = when (titleType) {
        EntryType.Anime -> AnimeStrings
        EntryType.Manga -> MangaStrings
    }

    CompositionLocalProvider(
        value = LocalTitleStrings provides strings,
        content = content
    )
}
