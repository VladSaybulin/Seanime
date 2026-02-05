package ru.vladsaybulin.core.ui2.strings

import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.userrate.UserRateStatus

interface DependsOnTitleStrings {
    fun titleStatusId(status: EntryStatus): Int

    fun userStatusId(status: UserRateStatus): Int
}