package ru.vladsaybulin.data.model

import ru.vladsaybulin.core.network.graphql.AnimeUserRateQuery
import ru.vladsaybulin.database.models.UserRateDbo
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.UserRate
import ru.vladsaybulin.model.UserRateValues
import ru.vladsaybulin.network.models.UserRateValuesDto
import ru.vladsaybulin.network.models.UserRateWithEntryLinkDto

fun AnimeUserRateQuery.UserRate.asUserRate() = UserRate(
    id = id,
    createdAt = createdAt,
    updatedAt = updatedAt,
    status = status.asUserRateStatus(),
    score = score,
    episodes = episodes,
    chapters = 0,
    volumes = 0,
    rewatches = rewatches,
    text = text ?: ""
)

fun UserRateValues.asDto() = UserRateValuesDto(
    status = status,
    score = score,
    episodes = episodes ?: 0,
    chapters = chapters ?: 0,
    volumes = volumes ?: 0,
    rewatches = rewatches,
    text = text
)

fun UserRateWithEntryLinkDto.asDbo() = UserRateDbo(
    id = id,
    animeId = when (entryType) {
        EntryType.Anime -> entryId
        else -> null
    },
    mangaId = when (entryType) {
        EntryType.Anime -> null
        else -> entryId
    },
    status = status,
    score = score,
    episodes = episodes,
    chapters = chapters,
    volumes = volumes,
    rewatches = rewatches,
    text = text,
    createdAt = createdAt,
    updatedAt = updatedAt
)