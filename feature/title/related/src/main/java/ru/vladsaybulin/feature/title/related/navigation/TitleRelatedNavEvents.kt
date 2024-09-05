package ru.vladsaybulin.feature.title.related.navigation

import ru.vladsaybulin.model.common.EntryType

class TitleRelatedNavEvents(
    val navigateToTitleDetails: (EntryType, Long) -> Unit,
    val navigateUp: () -> Unit
)