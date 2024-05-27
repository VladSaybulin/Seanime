package ru.vladsaybulin.feature.authors.navigation

import androidx.compose.runtime.Immutable

@Immutable
data class TitleAuthorsNavEvents(
    val navigateToPerson: (personId: Long) -> Unit,
    val navigateUp: () -> Unit
)