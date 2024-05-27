package ru.vladsaybulin.feature.home.navigation

import ru.vladsaybulin.model.common.EntryType

data class HomeNavEvents(
    val navigateToTitleDetails: (type: EntryType, id: Long) -> Unit,
    val navigateToSearchAnimeOngoing: () -> Unit,
    val navigateToUrl: (url: String) -> Unit,
    val navigateToAllNewsTopics: () -> Unit,
    val showUserRateEditor: (/* TODO EditableUserRate? */) -> Unit
)