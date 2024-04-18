package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.database.models.userrate.UserRateEntity
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.network.models.NetworkManga

fun Manga.asEntity() = MangaEntity(
    id = id,
    originalName = name,
    russianName = russianName,
    poster = poster?.asEntity(),
    kind = kind,
    status = status,
    score = score ?: 0f,
    chapters = chapters,
    volumes = volumes,
    airedOn = airedOn?.asEntity(),
    releasedOn = releasedOn?.asEntity()
)

fun NetworkManga.asEntity() = MangaEntity(
    id = id,
    originalName = originalName,
    russianName = russianName,
    poster = poster?.asEntity(),
    kind = kind,
    status = status,
    score = score ?: 0f,
    chapters = chapters,
    volumes = volumes,
    airedOn = airedOn?.asEntity(),
    releasedOn = releasedOn?.asEntity()
)

fun NetworkManga.userRateEntityShell() = userRate?.let { userRate ->
    UserRateEntity(
        id = userRate.id,
        animeId = id,
        mangaId = null,
        status = userRate.status,
        score = userRate.score,
        episodes = 0,
        chapters = chapters,
        volumes = volumes,
        rewatches = userRate.rewatches,
        text = userRate.text ?: "",
        createdAt = userRate.createdAt,
        updatedAt = userRate.updatedAt
    )
}

fun NetworkManga.asExternalModel() = Manga(
    id = id,
    name = originalName,
    russianName = russianName,
    poster = poster?.asPoster(),
    kind = kind,
    status = status,
    score = score ?: 0f,
    chapters = chapters,
    volumes = volumes,
    airedOn = airedOn?.asIncompleteDate(),
    releasedOn = releasedOn?.asIncompleteDate()
)