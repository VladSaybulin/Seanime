package ru.vladsaybulin.data.model

import ru.vladsaybulin.core.network.graphql.AnimeUserRateQuery
import ru.vladsaybulin.core.network.graphql.MangaUserRateQuery
import ru.vladsaybulin.database.models.userrate.UserRateEntity
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.UserRate
import ru.vladsaybulin.model.UserRateValues
import ru.vladsaybulin.network.models.CreateUserRateDto
import ru.vladsaybulin.network.models.UserRateDto
import ru.vladsaybulin.network.models.UserRateValuesDto
import ru.vladsaybulin.network.models.UserRateWithEntryDto
import ru.vladsaybulin.network.models.UserRateWithEntryLinkDto

fun UserRateDto.asExternalModel() = UserRate(
    id = id,
    createdAt = createdAt,
    updatedAt = updatedAt,
    status = status,
    score = score,
    episodes = episodes,
    chapters = 0,
    volumes = 0,
    rewatches = rewatches,
    text = text ?: ""
)

fun UserRateEntity.asUserRate() = UserRate(
    id = id,
    createdAt = createdAt,
    updatedAt = updatedAt,
    status = status,
    score = score,
    episodes = episodes,
    chapters = 0,
    volumes = 0,
    rewatches = rewatches,
    text = text
)

fun AnimeUserRateQuery.UserRate.asEntity(animeId: Long) = UserRateEntity(
    id = id,
    animeId = animeId,
    mangaId = null,
    status = status.asUserRateStatus(),
    score = score,
    episodes = episodes,
    chapters = 0,
    volumes = 0,
    rewatches = rewatches,
    text = text ?: "",
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun MangaUserRateQuery.UserRate.asEntity(mangaId: Long) = UserRateEntity(
    id = id,
    animeId = null,
    mangaId = mangaId,
    status = status.asUserRateStatus(),
    score = score,
    episodes = 0,
    chapters = chapters,
    volumes = volumes,
    rewatches = 0,
    text = text ?: "",
    createdAt = createdAt,
    updatedAt = updatedAt
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

fun UserRate.asEntity(
    entryType: EntryType,
    entryId: Long
) = UserRateEntity(
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

fun UserRateWithEntryLinkDto.asEntity() = UserRateEntity(
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

fun CreateUserRateDto(
    userId: Long,
    entryType: EntryType,
    entryId: Long,
    userRateValues: UserRateValues
) = with(userRateValues) {
    CreateUserRateDto(
        userId = userId,
        targetType = entryType,
        targetId = entryId,
        status = status,
        score = score,
        episodes = episodes ?: 0,
        chapters = chapters ?: 0,
        volumes = volumes ?: 0,
        rewatches = rewatches,
        text = text
    )
}

internal fun UserRateWithEntryDto.userRateDboShell() = with(userRateDto) {
    UserRateEntity(
        id = id,
        animeId = networkAnime?.id,
        mangaId = networkManga?.id,
        status = status,
        score = score,
        episodes = episodes,
        chapters = chapters,
        volumes = volumes,
        rewatches = rewatches,
        text = text ?: "",
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}