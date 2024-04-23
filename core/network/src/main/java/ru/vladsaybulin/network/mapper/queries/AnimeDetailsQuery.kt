package ru.vladsaybulin.network.mapper.queries

import ru.vladsaybulin.core.network.graphql.AnimeDetailsQuery
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.related.asRelationType
import ru.vladsaybulin.network.mapper.enums.asAnimeKind
import ru.vladsaybulin.network.mapper.enums.asAnimeRating
import ru.vladsaybulin.network.mapper.enums.asEntryStatus
import ru.vladsaybulin.network.mapper.enums.asGenreKind
import ru.vladsaybulin.network.mapper.enums.asMangaKind
import ru.vladsaybulin.network.mapper.enums.asUserRateStatus
import ru.vladsaybulin.network.mapper.enums.asVideoKind
import ru.vladsaybulin.network.models.NetworkAnime
import ru.vladsaybulin.network.models.NetworkGenre
import ru.vladsaybulin.network.models.NetworkIncompleteDate
import ru.vladsaybulin.network.models.NetworkManga
import ru.vladsaybulin.network.models.NetworkStudio
import ru.vladsaybulin.network.models.anime.NetworkAnimeDetails
import ru.vladsaybulin.network.models.anime.NetworkVideo
import ru.vladsaybulin.network.models.character.NetworkCharacter
import ru.vladsaybulin.network.models.character.NetworkCharacterWithRole
import ru.vladsaybulin.network.models.common.NetworkImage
import ru.vladsaybulin.network.models.common.NetworkStatisticsItem
import ru.vladsaybulin.network.models.person.NetworkPerson
import ru.vladsaybulin.network.models.person.NetworkPersonWithRoles
import ru.vladsaybulin.network.models.related.NetworkRelated

internal fun AnimeDetailsQuery.Anime.asNetworkModel() = NetworkAnimeDetails(
    id = id,
    name = name,
    nameRu = russian,
    nameEn = english,
    nameJp = japanese,
    alternativeName = synonyms,
    licenseNameRu = licenseNameRu,
    poster = poster?.run { NetworkImage(originalUrl, mini2xUrl) },
    kind = kind.asAnimeKind(),
    score = score?.toScore(),
    status = status.asEntryStatus(),
    rating = rating.asAnimeRating(),
    episodes = episodes,
    episodesAired = episodesAired,
    nextEpisodeAt = nextEpisodeAt,
    duration = duration,
    airedOn = airedOn?.run { NetworkIncompleteDate(day, month, year) },
    releasedOn = releasedOn?.run { NetworkIncompleteDate(day, month, year) },
    descriptionHtml = descriptionHtml?.takeIf { it.isNotBlank() },
    descriptionSource = descriptionSource?.takeIf { it.isNotBlank() },
    genres = genres?.map(AnimeDetailsQuery.Genre::asNetworkModel),
    subbers = fansubbers,
    dubbers = fandubbers,
    scoreStats = scoresStats?.map(AnimeDetailsQuery.ScoresStat::asNetworkModel),
    userRateStatusStats = statusesStats?.map(AnimeDetailsQuery.StatusesStat::asNetworkModel),
    studios = studios.map(AnimeDetailsQuery.Studio::asNetworkModel),
    authors = personRoles?.map(AnimeDetailsQuery.PersonRole::asNetworkModel),
    characters = characterRoles?.map(AnimeDetailsQuery.CharacterRole::asNetworkModel),
    related = related?.mapNotNull(AnimeDetailsQuery.Related::asNetworkModel),
    screenshots = screenshots.map(AnimeDetailsQuery.Screenshot::asNetworkModel),
    videos = videos.map(AnimeDetailsQuery.Video::asNetworkModel)
)

private fun Double?.toScore() = this?.toFloat() ?: 0f

private fun AnimeDetailsQuery.Genre.asNetworkModel() = NetworkGenre(
    id = id,
    name = name,
    russianName = russian,
    entryType = EntryType.Anime,
    kind = kind.asGenreKind()
)

private fun AnimeDetailsQuery.ScoresStat.asNetworkModel() = NetworkStatisticsItem(score, count)

private fun AnimeDetailsQuery.StatusesStat.asNetworkModel() =
    NetworkStatisticsItem(status.asUserRateStatus(), count)

private fun AnimeDetailsQuery.Studio.asNetworkModel() = NetworkStudio(
    id = id,
    name = name,
    image = imageUrl
)

private fun AnimeDetailsQuery.PersonRole.asNetworkModel() = NetworkPersonWithRoles(
    person = NetworkPerson(
        id = person.id,
        name = person.name,
        nameRu = person.russian,
        image = person.poster?.let {
            NetworkImage(
                originalUrl = it.originalUrl,
                previewUrl = it.main2xUrl
            )
        }
    ),
    roles = rolesEn,
    russianRoles = rolesRu
)

private fun AnimeDetailsQuery.CharacterRole.asNetworkModel() = NetworkCharacterWithRole(
    character = NetworkCharacter(
        id = character.id,
        name = character.name,
        nameRu = character.russian,
        image = character.poster?.let { NetworkImage(it.originalUrl, it.main2xUrl) }
    ),
    isMain = rolesEn.contains("Main")
)

private fun AnimeDetailsQuery.Related.asNetworkModel() = if (anime != null || manga != null) {
    NetworkRelated(
        anime = anime?.run {
            NetworkAnime(
                id = id,
                originalName = name,
                russianName = russian,
                poster = poster?.let { p ->
                    NetworkImage(
                        previewUrl = p.main2xUrl,
                        originalUrl = p.originalUrl
                    )
                },
                kind = kind.asAnimeKind(),
                status = status.asEntryStatus(),
                score = score.toScore(),
                episodes = episodes,
                episodesAired = episodesAired,
                airedOn = airedOn?.let { NetworkIncompleteDate(it.day, it.month, it.year) },
                releasedOn = releasedOn?.let { NetworkIncompleteDate(it.day, it.month, it.year) },
                userRate = null
            )
        },
        manga = manga?.run {
            NetworkManga(
                id = id,
                originalName = name,
                russianName = russian,
                poster = poster?.let { p ->
                    NetworkImage(
                        previewUrl = p.main2xUrl,
                        originalUrl = p.originalUrl
                    )
                },
                kind = kind.asMangaKind(),
                status = status.asEntryStatus(),
                score = score?.toScore(),
                chapters = chapters,
                volumes = volumes,
                airedOn = airedOn?.let { NetworkIncompleteDate(it.day, it.month, it.year) },
                releasedOn = releasedOn?.let { NetworkIncompleteDate(it.day, it.month, it.year) },
            )
        },
        relationType = relationEn.asRelationType()
    )
} else null

private fun AnimeDetailsQuery.Screenshot.asNetworkModel() = NetworkImage(
    previewUrl = x332Url,
    originalUrl = originalUrl
)

private fun AnimeDetailsQuery.Video.asNetworkModel() = NetworkVideo(
    name = name,
    previewImageUrl = "https:$imageUrl",
    videoUrl = url,
    playerUrl = "https:$imageUrl",
    kind = kind.asVideoKind()
)