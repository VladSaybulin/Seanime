package ru.vladsaybulin.feature.title.characters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.LocalScreenContentPadding
import ru.vladsaybulin.core.ui.entry.EntryGridItem
import ru.vladsaybulin.feature.title.characters.navigation.TitleCharactersNavEvents
import ru.vladsaybulin.model.character.Character

@Composable
fun TitleCharactersRoute(
    navEvents: TitleCharactersNavEvents,
    viewModel: TitleCharacterViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    TitleCharactersScreen(state = state, navEvents = navEvents)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TitleCharactersScreen(
    state: TitleCharactersUiState,
    navEvents: TitleCharactersNavEvents
) {
    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            TitleCharactersTopBar(
                onBackClick = navEvents.navigateUp,
                scrollBehavior = topAppBarScrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
    ) { scaffoldPadding ->
        Box(
            modifier = Modifier
                .padding(scaffoldPadding)
                .padding(LocalScreenContentPadding.current)
        ) {
            if (state is TitleCharactersUiState.Success) {
                TitleCharactersContent(
                    state = state,
                    onCharacterClick = navEvents.navigateToCharacterDetails
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TitleCharactersTopBar(
    onBackClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.feature_title_characters_title)) },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = SeanimeIcons.ArrowBack,
                    contentDescription = stringResource(id = R.string.feature_title_characters_back)
                )
            }
        }
    )
}

@Composable
private fun TitleCharactersContent(
    state: TitleCharactersUiState.Success,
    onCharacterClick: (Long) -> Unit
) {

    val characters = state.characters
    val firstMinorCharacter = characters.indexOfFirst { !it.isMain }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(96.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        if (characters.first().isMain) {
            header {
                Text(
                    text = stringResource(id = R.string.feature_title_characters_main_charcaters),
                    style = SeanimeTheme.typography.labelLarge,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            items(count = firstMinorCharacter) { index ->
                val character = characters[index].character
                CharacterCard(
                    character = character,
                    onClick = { onCharacterClick(character.id) }
                )
            }
        }

        if (firstMinorCharacter != -1) {
            if (characters.first().isMain) {
                item(span = {GridItemSpan(maxLineSpan)}) { Spacer(modifier = Modifier.height(16.dp)) }
            }

            header {
                Text(
                    text = stringResource(id = R.string.feature_title_characters_main_charcaters),
                    style = SeanimeTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 32.dp, bottom = 4.dp)
                )
            }

            items(count = characters.size - firstMinorCharacter) { index ->
                val character = characters[firstMinorCharacter + index].character
                CharacterCard(
                    character = character,
                    onClick = { onCharacterClick(character.id) }
                )
            }
        }
    }
}

@Composable
private fun CharacterCard(character: Character, onClick: () -> Unit) {
    EntryGridItem(
        name = character.run { russianName ?: originalName },
        imageUrl = character.poster?.originalUrl,
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    )
}

fun LazyGridScope.header(
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
}