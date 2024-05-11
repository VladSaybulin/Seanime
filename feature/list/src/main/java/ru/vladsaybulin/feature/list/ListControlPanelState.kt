package ru.vladsaybulin.feature.list

import androidx.compose.runtime.Immutable
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateStatus


@Immutable
internal data class ListControlPanelState(
    val entryType: EntryType,
    val userRateStatus: UserRateStatus
)