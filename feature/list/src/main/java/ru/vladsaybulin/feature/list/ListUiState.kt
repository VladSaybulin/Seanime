package ru.vladsaybulin.feature.list

import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateStatus

data class ListUiState(
    val entryType: EntryType,
    val userRateStatus: UserRateStatus
)