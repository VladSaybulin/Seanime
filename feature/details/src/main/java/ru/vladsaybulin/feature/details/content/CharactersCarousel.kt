package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.vladsaybulin.core.designsystem.components.ShikimoriCarousel
import ru.vladsaybulin.core.ui.ContentWithClickableHeader
import ru.vladsaybulin.feature.details.R
import ru.vladsaybulin.model.CharacterWithRole

@Composable
fun CharactersCarousel(
    characters: ImmutableList<CharacterWithRole>,
    onCharacterClick: (Long) -> Unit,
    onShowAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val mainSize = characters.indexOfFirst { !it.isMain }.takeIf { it != -1 }
        ?: characters.size
    val shownCharacters = characters.subList(0, mainSize)

    val shouldShownShowAll = mainSize < characters.size

    ContentWithClickableHeader(
        headerText = {
            ShowAllHeaderText(
                headerText = stringResource(id = R.string.characters),
                shouldShownShowAll = shouldShownShowAll,
            )
        },
        onClick = onShowAllClick,
        modifier = modifier,
        enabled = shouldShownShowAll
    ) {
        ShikimoriCarousel(
            items = shownCharacters,
            key = { it.character.id }
        ) { character ->
            CharacterCard(
                character = character.character,
                onClick = { onCharacterClick(character.character.id) },
                modifier = Modifier.width(CharacterCardWidth)
            )
        }
    }
}

private val CharacterCardWidth = 96.dp