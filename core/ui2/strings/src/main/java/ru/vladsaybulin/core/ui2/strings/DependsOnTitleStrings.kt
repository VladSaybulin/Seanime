package ru.vladsaybulin.core.ui2.strings

import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateStatus

interface DependsOnTitleStrings {
    val titleType: EntryType

    fun titleStatusId(status: EntryStatus): Int

    fun userStatusId(status: UserRateStatus): Int
}