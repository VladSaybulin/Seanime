package ru.vladsaybulin.feature.titlerelated.navigation

import ru.vladsaybulin.model.common.EntryType

class TitleRelatedNavEvents(
    val navigateToTitleDetails: (EntryType, Long) -> Unit,
    val navigateUp: () -> Unit
)