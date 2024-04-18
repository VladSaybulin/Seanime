package ru.vladsaybulin.core.navigation

import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType

data class SearchArgs(
    val entryType: EntryType? = null,
    val entryStatus: EntryStatus? = null,
    val genreId: Long? = null,
    val studioId: Long? = null,
    val publisherId: Long? = null
)