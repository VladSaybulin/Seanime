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
import ru.vladsaybulin.ui2.entry.EntryCarousel
import ru.vladsaybulin.ui2.entry.EntryGridItem

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
            CharacterCard(character, { onCharacterClick(character) })
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