package ru.vladsaybulin.core.navigation

import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.model.EntryType

data class SearchArgs(
    val entryType: EntryType? = null,
    val entryStatus: EntryStatus? = null,
    val genreId: Long? = null,
    val studioId: Long? = null,
    val publisherId: Long? = null
)