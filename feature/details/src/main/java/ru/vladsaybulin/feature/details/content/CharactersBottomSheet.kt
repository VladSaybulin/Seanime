package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.components.ShikimoriModalBottomSheet
import ru.vladsaybulin.core.ui.Header
import ru.vladsaybulin.feature.details.R
import ru.vladsaybulin.model.CharacterWithRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CharactersBottomSheet(
    allCharacters: List<CharacterWithRole>,
    onCharacterClick: (Long) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    ShikimoriModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        CharactersBottomSheetContent(
            allCharacters = allCharacters,
            onCharacterClick = onCharacterClick
        )
    }
}

@Composable
private fun CharactersBottomSheetContent(
    allCharacters: List<CharacterWithRole>,
    onCharacterClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier.padding(horizontal = 16.dp),
        columns = GridCells.Adaptive(96.dp),
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val mainCharactersSize = allCharacters.indexOfFirst { !it.isMain }
            .takeIf { it != -1 } ?: allCharacters.size

        if (mainCharactersSize > 0) {
            item(key = "main_header", span = { GridItemSpan(maxLineSpan) }) {
                Header {
                    Text(text = stringResource(id = R.string.main_characters))
                }
            }

            items(
                count = mainCharactersSize,
                key = { index -> allCharacters[index].character.id }
            ) { index ->
                val character = allCharacters[index].character
                CharacterCard(
                    character = character,
                    onClick = { onCharacterClick(character.id) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (allCharacters.size - mainCharactersSize > 0) {
            item(key = "minor_header", span = { GridItemSpan(maxLineSpan) }) {
                Header {
                    Text(text = stringResource(id = R.string.minor_characters))
                }
            }

            items(
                count = allCharacters.size - mainCharactersSize,
                key = { index -> allCharacters[mainCharactersSize + index].character.id }
            ) { index ->
                val character = allCharacters[mainCharactersSize + index].character
                CharacterCard(
                    character = character,
                    onClick = { onCharacterClick(character.id) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}