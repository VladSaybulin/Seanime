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
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui2.strings.AnimeStrings
import ru.vladsaybulin.core.ui2.strings.R
import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.anime.AnimeRating

@Composable
@ReadOnlyComposable
fun AnimeKind.asString(): String =
    stringResource(id = AnimeStrings.kindId(this))

@Composable
@ReadOnlyComposable
fun AnimeKind.asStringOrNull(): String? {
    if (this == AnimeKind.None) return null
    return asString()
}

@Composable
@ReadOnlyComposable
fun AnimeRating.asString() =
    stringResource(id = AnimeStrings.ratingId(this))

@Composable
@ReadOnlyComposable
fun AnimeStrings.ProgressFormat.asString(): String {
    return when (this) {
        is AnimeStrings.ProgressFormat.AiredOfTotal -> stringResource(
            R.string.core_ui2_strings_anime_progress_aired_of_total,
            aired,
            total
        )

        is AnimeStrings.ProgressFormat.AiredOfUnknown -> stringResource(
            R.string.core_ui2_strings_anime_progress_aired_of_unknown,
            aired
        )

        is AnimeStrings.ProgressFormat.TotalOnly -> stringResource(
            R.string.core_ui2_strings_anime_progress_total_only,
            total
        )
    }
}
