package ru.vladsaybulin.feature.character

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import ru.vladsaybulin.core.navigation.args.EntryDetailsArgs

@Composable
fun CharacterDetailsScreen(
    viewModel: CharacterDetailsViewModel = hiltViewModel(),
    onEntryClick: (EntryDetailsArgs) -> Unit,
    onPersonClick: (Long) -> Unit,
) {



}