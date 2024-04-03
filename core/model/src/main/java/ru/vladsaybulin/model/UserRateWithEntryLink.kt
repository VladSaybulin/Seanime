package ru.vladsaybulin.model

class UserRateWithEntryLink(
    val entryType: EntryType,
    val entryId: Long,
    val userRate: UserRate
)