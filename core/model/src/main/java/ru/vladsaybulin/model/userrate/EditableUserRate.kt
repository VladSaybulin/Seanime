package ru.vladsaybulin.model.userrate

import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType

data class EditableUserRate(
    val userRate: UserRate,
    val titleType: EntryType,
    val entryStatus: EntryStatus,
    val maxEpisodes: Int,
    val maxChapters: Int,
    val maxVolumes: Int
)