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

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.model.character.Character
import ru.vladsaybulin.core.ui2.entry.EntryCarousel
import ru.vladsaybulin.core.ui2.entry.EntryGridItem
import ru.vladsaybulin.core.ui2.entry.character.CharacterItem

@Composable
internal fun TitleCharacters(
    characters: List<Character>,
    onCharacterClick: (Character) -> Unit
) {
    val listState = rememberLazyListState()

    EntryCarousel(
        state = listState,
        flingBehavior = rememberSnapFlingBehavior(
            lazyListState = listState,
            snapPosition = SnapPosition.Start
        )
    ) {
        items(items = characters) { character ->
            CharacterItem(
                character = character,
                onClick = { onCharacterClick(character) },
                modifier = Modifier.width(CharacterCardWidth)
            )
        }
    }
}

@Composable
private fun CharacterCard(
    character: Character,
    onClick: () -> Unit
) {
    EntryGridItem(
        name = character.originalName,
        russianName = character.russianName,
        poster = character.poster,
        onClick = onClick,
        modifier = Modifier.width(CharacterCardWidth)
    )
}

private val CharacterCardWidth = 96.dp