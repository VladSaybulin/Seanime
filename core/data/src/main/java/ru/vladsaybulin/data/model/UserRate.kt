package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.userrate.UserRateEntity
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRate
import ru.vladsaybulin.model.userrate.UserRateValues
import ru.vladsaybulin.network.models.userrate.CreateUserRateRequest
import ru.vladsaybulin.network.models.userrate.NetworkUserRate
import ru.vladsaybulin.network.models.userrate.UpdateUserRateRequest
import ru.vladsaybulin.network.models.userrate.NetworkUserRateWithTitle
import ru.vladsaybulin.network.models.userrate.NetworkUserRateWithTitleLink

fun NetworkUserRate.asEntity(
    animeId: Long? = null,
    mangaId: Long? = null
) = UserRateEntity(
    id = id,
    animeId = animeId,
    mangaId = mangaId,
    status = status,
    score = score,
    episodes = episodes,
    chapters = chapters,
    volumes = volumes,
    rewatches = rewatches,
    text = text ?: "",
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun NetworkUserRateWithTitle.asEntity() =
    networkUserRate.asEntity(animeId = networkAnime?.id, mangaId = networkManga?.id)

fun NetworkUserRate.asExternalModel() = UserRate(
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

fun UserRateValues.asDto() = UpdateUserRateRequest(
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

fun NetworkUserRateWithTitleLink.asEntity() = UserRateEntity(
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
    text = text ?: "",
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun CreateUserRateRequest(
    userId: Long,
    entryType: EntryType,
    entryId: Long,
    userRateValues: UserRateValues
) = with(userRateValues) {
    CreateUserRateRequest(
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

internal fun NetworkUserRateWithTitle.userRateEntityShell() = with(networkUserRate) {
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

internal fun NetworkUserRateWithTitle.animeEntityOrNullShells() =
    networkAnime?.asEntity()

internal fun NetworkUserRateWithTitle.mangaEntityOrNullShells() =
    networkManga?.asEntity()