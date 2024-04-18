package ru.vladsaybulin.feature.details.content

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.entry.EntryGridItem
import ru.vladsaybulin.model.character.Character

@Composable
fun CharacterCard(
    character: Character,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    EntryGridItem(
        name = character.run { russianName ?: originalName },
        poster = character.poster,
        onClick = onClick,
        modifier = modifier,
        nameTextStyle = ShikimoriTheme.typography.labelSmall
    )
}