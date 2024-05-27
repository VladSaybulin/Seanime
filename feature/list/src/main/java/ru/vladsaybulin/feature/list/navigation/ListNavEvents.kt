package ru.vladsaybulin.feature.list.navigation

import ru.vladsaybulin.model.common.EntryType

data class ListNavEvents(
    val navigateToTitleDetails: (type: EntryType, id: Long) -> Unit,
    val showUserRateEditor: (/* TODO EditingUserRate? */) -> Unit,
    val startAuthorization: () -> Unit,
    val navigateUp: () -> Unit
)