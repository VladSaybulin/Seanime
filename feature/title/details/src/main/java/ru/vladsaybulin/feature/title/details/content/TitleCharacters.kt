package ru.vladsaybulin.feature.title.details.content

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.components.ShikimoriCarousel
import ru.vladsaybulin.core.designsystem.components.ShikimoriCarouselDefaults
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.entry.EntryGridItem
import ru.vladsaybulin.core.ui.entry.EntryGridItemDefaults
import ru.vladsaybulin.model.character.Character
import ru.vladsaybulin.model.character.CharacterWithRole

@Composable
internal fun TitleCharacters(
    characters: List<Character>,
    onCharacterClick: (Character) -> Unit
) {
    val listState = rememberLazyListState()

    ShikimoriCarousel(
        items = characters,
        listState = listState,
        flingBehavior = rememberSnapFlingBehavior(
            lazyListState = listState,
            snapPosition = SnapPosition.Start
        )
    ) { character ->
        CharacterCard(
            character = character,
            onClick = { onCharacterClick(character) }
        )
    }
}

@Composable
private fun CharacterCard(
    character: Character,
    onClick: () -> Unit
) {
    EntryGridItem(
        name = character.run { russianName ?: originalName },
        imageUrl = character.poster?.originalUrl,
        onClick = onClick,
        modifier = Modifier.width(CharacterCardWidth),
        nameTextStyle = SeanimeTheme.typography.labelSmall
    )
}

private val CharacterCardWidth = 96.dp