package ru.vladsaybulin.feature.list.navigation

import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.EditableUserRate

data class ListNavEvents(
    val navigateToTitleDetails: (type: EntryType, id: Long) -> Unit,
    val showUserRateEditor: (EditableUserRate) -> Unit,
    val startAuthorization: () -> Unit,
    val navigateUp: () -> Unit
)