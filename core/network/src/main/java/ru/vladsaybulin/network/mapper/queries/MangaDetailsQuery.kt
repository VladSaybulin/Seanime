package ru.vladsaybulin.network.mapper.queries

import ru.vladsaybulin.core.network.graphql.MangaDetailsQuery
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.network.mapper.enums.asAnimeKind
import ru.vladsaybulin.network.mapper.enums.asEntryStatus
import ru.vladsaybulin.network.mapper.enums.asGenreKind
import ru.vladsaybulin.network.mapper.enums.asMangaKind
import ru.vladsaybulin.network.mapper.enums.asRelationType
import ru.vladsaybulin.network.mapper.enums.asUserRateStatus
import ru.vladsaybulin.network.models.NetworkAnime
import ru.vladsaybulin.network.models.NetworkGenre
import ru.vladsaybulin.network.models.NetworkIncompleteDate
import ru.vladsaybulin.network.models.NetworkManga
import ru.vladsaybulin.network.models.NetworkPublisher
import ru.vladsaybulin.network.models.character.NetworkCharacter
import ru.vladsaybulin.network.models.character.NetworkCharacterWithRole
import ru.vladsaybulin.network.models.common.NetworkImage
import ru.vladsaybulin.network.models.common.NetworkStatisticsItem
import ru.vladsaybulin.network.models.manga.NetworkMangaDetails
import ru.vladsaybulin.network.models.person.NetworkPerson
import ru.vladsaybulin.network.models.person.NetworkPersonWithRoles
import ru.vladsaybulin.network.models.related.NetworkRelated

internal fun MangaDetailsQuery.Manga.asNetworkModel() = NetworkMangaDetails(
    id = id,
    name = name,
    nameRu = russian,
    nameEn = english,
    nameJp = japanese,
    alternativeName = synonyms,
    licenseNameRu = licenseNameRu,
    poster = poster?.asNetworkModel(),
    kind = kind.asMangaKind(),
    score = score?.toScore(),
    status = status.asEntryStatus(),
    chapters = chapters,
    volumes = volumes,
    airedOn = airedOn?.run { NetworkIncompleteDate(day, month, year) },
    releasedOn = releasedOn?.run { NetworkIncompleteDate(day, month, year) },
    descriptionHtml = descriptionHtml?.takeIf { it.isNotBlank() },
    descriptionSource = descriptionSource?.takeIf { it.isNotBlank() },
    genres = genres?.map(MangaDetailsQuery.Genre::asNetworkModel),
    scoreStats = scoresStats?.map(MangaDetailsQuery.ScoresStat::asNetworkModel),
    userRateStatusStats = statusesStats?.map(MangaDetailsQuery.StatusesStat::asNetworkModel),
    publishers = publishers.map(MangaDetailsQuery.Publisher::asNetworkModel),
    authors = personRoles?.map(MangaDetailsQuery.PersonRole::asNetworkModel),
    characters = characterRoles?.map(MangaDetailsQuery.CharacterRole::asCharacterWithRole),
    related = related?.mapNotNull(MangaDetailsQuery.Related::asNetworkModel),
)

private fun Double?.toScore() = this?.toFloat() ?: 0f

private fun MangaDetailsQuery.Poster.asNetworkModel() = NetworkImage(
    originalUrl = originalUrl,
    previewUrl = main2xUrl
)

private fun MangaDetailsQuery.Genre.asNetworkModel() = NetworkGenre(
    id = id,
    name = name,
    russianName = russian,
    entryType = EntryType.Manga,
    kind = kind.asGenreKind()
)

private fun MangaDetailsQuery.ScoresStat.asNetworkModel() = NetworkStatisticsItem(score, count)

private fun MangaDetailsQuery.StatusesStat.asNetworkModel() =
    NetworkStatisticsItem(status.asUserRateStatus(), count)

private fun MangaDetailsQuery.Publisher.asNetworkModel() = NetworkPublisher(
    id = id,
    name = name
)

private fun MangaDetailsQuery.PersonRole.asNetworkModel() = NetworkPersonWithRoles(
    person = NetworkPerson(
        id = person.id,
        name = person.name,
        nameRu = person.russian,
        image = person.poster?.let {
            NetworkImage(originalUrl = it.originalUrl, previewUrl = it.main2xUrl)
        }
    ),
    roles = rolesEn
)

private fun MangaDetailsQuery.CharacterRole.asCharacterWithRole() = NetworkCharacterWithRole(
    character = NetworkCharacter(
        id = character.id,
        name = character.name,
        nameRu = character.russian,
        image = character.poster?.let {
            NetworkImage(originalUrl = it.originalUrl, previewUrl = it.main2xUrl)
        }
    ),
    isMain = rolesEn.contains("Main")
)

fun MangaDetailsQuery.Related.asNetworkModel() = if (anime != null || manga != null) {
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
        relationType = relationKind.asRelationType()
    )
} else null
