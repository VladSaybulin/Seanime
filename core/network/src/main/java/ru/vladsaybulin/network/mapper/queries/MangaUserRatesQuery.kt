package ru.vladsaybulin.network.mapper.queries

import ru.vladsaybulin.core.network.graphql.MangaUserRatesQuery
import ru.vladsaybulin.network.mapper.enums.asEntryStatus
import ru.vladsaybulin.network.mapper.enums.asMangaKind
import ru.vladsaybulin.network.mapper.enums.asUserRateStatus
import ru.vladsaybulin.network.models.IncompleteDateDto
import ru.vladsaybulin.network.models.NetworkManga
import ru.vladsaybulin.network.models.PosterDto
import ru.vladsaybulin.network.models.UserRateDto
import ru.vladsaybulin.network.models.UserRateWithEntryDto

internal fun MangaUserRatesQuery.UserRate.asDto(): UserRateWithEntryDto {
    require(manga != null)
    return UserRateWithEntryDto(
        userRateDto = UserRateDto(
            id = id,
            createdAt = createdAt,
            updatedAt = updatedAt,
            status = status.asUserRateStatus(),
            score = score,
            episodes = 0,
            chapters = chapters,
            volumes = volumes,
            rewatches = rewatches,
            text = text ?: ""
        ),
        networkManga = manga!!.asDto(),
        networkAnime = null
    )
}

private fun MangaUserRatesQuery.Manga.asDto() = NetworkManga(
    id = id,
    originalName = name,
    russianName = russian,
    poster = poster?.asDto(),
    kind = kind.asMangaKind(),
    status = status.asEntryStatus(),
    score = score?.toFloat() ?: 0f,
    chapters = chapters,
    volumes = volumes,
    airedOn = airedOn?.asDto(),
    releasedOn = releasedOn?.asDto()
)

private fun MangaUserRatesQuery.Poster.asDto() = PosterDto(originalUrl, previewUrl)

private fun MangaUserRatesQuery.AiredOn.asDto() = IncompleteDateDto(day, month, year)

private fun MangaUserRatesQuery.ReleasedOn.asDto() = IncompleteDateDto(day, month, year)