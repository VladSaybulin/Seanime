package ru.vladsaybulin.network.mapper.queries

import ru.vladsaybulin.core.network.graphql.MangaDetailsQuery
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.network.mapper.enums.asEntryStatus
import ru.vladsaybulin.network.mapper.enums.asGenreKind
import ru.vladsaybulin.network.mapper.enums.asMangaKind
import ru.vladsaybulin.network.mapper.enums.asRelationType
import ru.vladsaybulin.network.mapper.enums.asUserRateStatus
import ru.vladsaybulin.network.mapper.fragments.asNetworkModel
import ru.vladsaybulin.network.models.common.NetworkGenre
import ru.vladsaybulin.network.models.manga.NetworkPublisher
import ru.vladsaybulin.network.models.common.NetworkStatisticsItem
import ru.vladsaybulin.network.models.manga.NetworkMangaDetails
import ru.vladsaybulin.network.models.related.NetworkRelated

internal fun MangaDetailsQuery.Manga.asNetworkModel() = NetworkMangaDetails(
    id = id,
    name = name,
    nameRu = russian,
    nameEn = english,
    nameJp = japanese,
    alternativeName = synonyms,
    licenseNameRu = licenseNameRu,
    poster = poster?.posterFragment?.asNetworkModel(),
    kind = kind.asMangaKind(),
    score = score?.toFloat() ?: 0f,
    status = status.asEntryStatus(),
    chapters = chapters,
    volumes = volumes,
    airedOn = airedOn?.incompleteDateFragment?.asNetworkModel(),
    releasedOn = releasedOn?.incompleteDateFragment?.asNetworkModel(),
    descriptionHtml = descriptionHtml?.takeIf { it.isNotBlank() },
    descriptionSource = descriptionSource?.takeIf { it.isNotBlank() },
    genres = genres?.map(MangaDetailsQuery.Genre::asNetworkModel),
    scoreStats = scoresStats?.map(MangaDetailsQuery.ScoresStat::asNetworkModel),
    userRateStatusStats = statusesStats?.map(MangaDetailsQuery.StatusesStat::asNetworkModel),
    publishers = publishers.map(MangaDetailsQuery.Publisher::asNetworkModel),
    related = related?.mapNotNull(MangaDetailsQuery.Related::asNetworkModel),
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

private fun MangaDetailsQuery.Related.asNetworkModel() = if (anime != null || manga != null) {
    NetworkRelated(
        anime = anime?.animeFragment?.asNetworkModel(),
        manga = manga?.mangaFragment?.asNetworkModel(),
        relationType = relationKind.asRelationType()
    )
} else null
