package ru.vladsaybulin.feature.list

import androidx.compose.runtime.Immutable
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.list.UserRateOrder
import ru.vladsaybulin.model.list.UserRateOrderField
import ru.vladsaybulin.model.userrate.UserRateStatus

@Immutable
internal data class ListControlPanelState(
    val entryType: EntryType,
    val userRateStatus: UserRateStatus,
    val orderField: UserRateOrderField,
    val order: UserRateOrder
)