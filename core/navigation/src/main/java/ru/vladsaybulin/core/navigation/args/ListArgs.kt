package ru.vladsaybulin.core.navigation.args

import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateStatus

data class ListArgs(
    val entryType: EntryType?,
    val userRateStatus: UserRateStatus?
)