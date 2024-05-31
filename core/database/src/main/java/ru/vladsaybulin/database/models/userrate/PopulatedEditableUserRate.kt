package ru.vladsaybulin.database.models.userrate

import androidx.room.Embedded
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.EditableUserRate

class PopulatedEditableUserRate(
    @Embedded val userRate: UserRateEntity,
    val entryStatus: EntryStatus,
    val maxEpisodes: Int,
    val maxChapters: Int,
    val maxVolumes: Int
)

fun PopulatedEditableUserRate.asExternalModel(titleType: EntryType) = EditableUserRate(
    userRate = userRate.asExternalModel(),
    titleType = titleType,
    entryStatus = entryStatus,
    maxEpisodes = maxEpisodes,
    maxVolumes = maxVolumes,
    maxChapters = maxChapters
)