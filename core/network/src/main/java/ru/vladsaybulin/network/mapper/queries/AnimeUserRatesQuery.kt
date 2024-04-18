package ru.vladsaybulin.network.mapper.queries

import ru.vladsaybulin.core.network.graphql.AnimeUserRatesQuery
import ru.vladsaybulin.network.mapper.enums.asUserRateStatus
import ru.vladsaybulin.network.mapper.enums.asAnimeKind
import ru.vladsaybulin.network.mapper.enums.asEntryStatus
import ru.vladsaybulin.network.models.NetworkAnime
import ru.vladsaybulin.network.models.IncompleteDateDto
import ru.vladsaybulin.network.models.PosterDto
import ru.vladsaybulin.network.models.UserRateDto
import ru.vladsaybulin.network.models.UserRateWithEntryDto

internal fun AnimeUserRatesQuery.UserRate.asDto(): UserRateWithEntryDto {
    require(anime != null)
    return UserRateWithEntryDto(
        userRateDto = UserRateDto(
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
        ),
        networkAnime = anime!!.asDto(),
        networkManga = null
    )
}

private fun AnimeUserRatesQuery.Anime.asDto() = NetworkAnime(
    id = id,
    originalName = name,
    russianName = russian,
    poster = poster?.asDto(),
    kind = kind.asAnimeKind(),
    status = status.asEntryStatus(),
    score = score?.toFloat() ?: 0f,
    episodes = episodes,
    episodesAired = episodesAired,
    airedOn = airedOn?.asDto(),
    releasedOn = releasedOn?.asDto()
)

private fun AnimeUserRatesQuery.Poster.asDto() = PosterDto(originalUrl, previewUrl)

private fun AnimeUserRatesQuery.AiredOn.asDto() = IncompleteDateDto(day, month, year)

private fun AnimeUserRatesQuery.ReleasedOn.asDto() = IncompleteDateDto(day, month, year)