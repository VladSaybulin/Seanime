package ru.vladsaybulin.feature.title.characters.navigation

class TitleCharactersNavEvents(
    val navigateToCharacterDetails: (Long) -> Unit,
    val navigateUp: () -> Unit
)
