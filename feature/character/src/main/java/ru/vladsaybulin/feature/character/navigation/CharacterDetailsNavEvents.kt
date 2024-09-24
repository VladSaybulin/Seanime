package ru.vladsaybulin.feature.character.navigation

data class CharacterDetailsNavEvents(
    val navigateToAnimeDetails: (Long) -> Unit,
    val navigateToMangaDetails: (Long) -> Unit,
    val navigateToCharacterDetails: (Long) -> Unit,
    val navigateToPersonDetails: (Long) -> Unit,
    val navigateUp: () -> Unit
)
