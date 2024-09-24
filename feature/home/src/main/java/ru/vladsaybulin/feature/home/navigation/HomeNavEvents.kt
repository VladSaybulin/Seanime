package ru.vladsaybulin.feature.home.navigation

import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.EditableUserRate

data class HomeNavEvents(
    val navigateToTitleDetails: (type: EntryType, id: Long) -> Unit,
    val navigateToSearchAnimeOngoing: () -> Unit,
    val navigateToMe: () -> Unit,
    val showUserRateEditor: (EditableUserRate) -> Unit
)