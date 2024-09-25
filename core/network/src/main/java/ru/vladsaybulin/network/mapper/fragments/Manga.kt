package ru.vladsaybulin.network.mapper.fragments

import ru.vladsaybulin.core.network.graphql.fragment.MangaFragment
import ru.vladsaybulin.core.network.graphql.fragment.MangaWithLocalDateFragment
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.network.mapper.enums.asEntryStatus
import ru.vladsaybulin.network.mapper.enums.asMangaKind
import ru.vladsaybulin.network.models.manga.NetworkManga
import ru.vladsaybulin.network.models.userrate.NetworkUserRate

internal fun MangaFragment.asNetworkModel(userRate: NetworkUserRate? = null) = NetworkManga(
    id = baseMangaFragment.id,
    originalName = baseMangaFragment.name,
    russianName = baseMangaFragment.russian,
    poster = baseMangaFragment.poster?.posterFragment?.asNetworkModel(),
    kind = baseMangaFragment.kind.asMangaKind(),
    status = baseMangaFragment.status?.asEntryStatus() ?: EntryStatus.None,
    score = baseMangaFragment.score?.toFloat() ?: 0f,
    chapters = baseMangaFragment.chapters,
    volumes = baseMangaFragment.volumes,
    airedOn = airedOn?.incompleteDateFragment?.asNetworkModel(),
    releasedOn = releasedOn?.incompleteDateFragment?.asNetworkModel(),
    userRate = userRate
)

internal fun MangaWithLocalDateFragment.asNetworkModel(userRate: NetworkUserRate? = null) = NetworkManga(
    id = baseMangaFragment.id,
    originalName = baseMangaFragment.name,
    russianName = baseMangaFragment.russian,
    poster = baseMangaFragment.poster?.posterFragment?.asNetworkModel(),
    kind = baseMangaFragment.kind.asMangaKind(),
    status = baseMangaFragment.status?.asEntryStatus() ?: EntryStatus.None,
    score = baseMangaFragment.score?.toFloat() ?: 0f,
    chapters = baseMangaFragment.chapters,
    volumes = baseMangaFragment.volumes,
    airedOn = airedOn?.date?.asIncompleteDate(),
    releasedOn = releasedOn?.date?.asIncompleteDate(),
    userRate = userRate
)