package ru.vladsaybulin.feature.search.navigation

data class SearchNavEvents(
    val navigateToAnime: (animeId: Long) -> Unit,
    val navigateToManga: (mangaId: Long) -> Unit
)