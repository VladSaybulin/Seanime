package ru.vladsaybulin.model.userrate

import ru.vladsaybulin.model.common.EntryType

class UserRateWithEntryLink(
    val entryType: EntryType,
    val entryId: Long,
    val userRate: UserRate
)