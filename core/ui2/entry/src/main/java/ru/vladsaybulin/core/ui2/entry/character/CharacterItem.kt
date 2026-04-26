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

package ru.vladsaybulin.core.ui2.entry.character

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.model.character.Character
import ru.vladsaybulin.core.ui2.entry.EntryCarouselItem
import ru.vladsaybulin.core.ui2.entry.EntryItemDefaults

@Composable
fun CharacterItem(
    character: Character,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    EntryCarouselItem(
        name = character.originalName,
        russianName = character.russianName,
        poster = character.poster,
        onClick = onClick,
        modifier = modifier,
        colors = EntryItemDefaults.SurfaceColors,
        infoPadding = PaddingValues(vertical = 4.dp)
    )
}