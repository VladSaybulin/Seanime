package ru.vladsaybulin.core.navigation.args

import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.search.SearchType

data class SearchArgs(
    val searchType: SearchType? = null,
    val entryStatus: EntryStatus? = null,
    val genreId: Long? = null,
    val demographicId: Long? = null,
    val themeId: Long? = null,
    val studioId: Long? = null,
    val publisherId: Long? = null
)