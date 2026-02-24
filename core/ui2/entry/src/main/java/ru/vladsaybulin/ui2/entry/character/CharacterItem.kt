package ru.vladsaybulin.ui2.entry.character

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.model.character.Character
import ru.vladsaybulin.ui2.entry.EntryCarouselItem
import ru.vladsaybulin.ui2.entry.EntryItemDefaults

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