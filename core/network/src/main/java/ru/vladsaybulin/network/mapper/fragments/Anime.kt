package ru.vladsaybulin.network.mapper.fragments

import ru.vladsaybulin.core.network.graphql.fragment.AnimeFragment
import ru.vladsaybulin.core.network.graphql.fragment.AnimeWithLocalDateFragment
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.network.mapper.enums.asAnimeKind
import ru.vladsaybulin.network.mapper.enums.asEntryStatus
import ru.vladsaybulin.network.models.anime.NetworkAnime
import ru.vladsaybulin.network.models.userrate.NetworkUserRate

internal fun AnimeFragment.asNetworkModel(userRate: NetworkUserRate? = null) = NetworkAnime(
    id = baseAnimeFragment.id,
    originalName = baseAnimeFragment.name,
    russianName = baseAnimeFragment.russian,
    poster = baseAnimeFragment.poster?.posterFragment?.asNetworkModel(),
    kind = baseAnimeFragment.kind.asAnimeKind(),
    status = baseAnimeFragment.status?.asEntryStatus() ?: EntryStatus.None,
    score = baseAnimeFragment.score?.toFloat() ?: 0f,
    episodes = baseAnimeFragment.episodes,
    episodesAired = baseAnimeFragment.episodesAired,
    airedOn = airedOn?.incompleteDateFragment?.asNetworkModel(),
    releasedOn = releasedOn?.incompleteDateFragment?.asNetworkModel(),
    userRate = userRate
)

internal fun AnimeWithLocalDateFragment.asNetworkModel(userRate: NetworkUserRate? = null) = NetworkAnime(
    id = baseAnimeFragment.id,
    originalName = baseAnimeFragment.name,
    russianName = baseAnimeFragment.russian,
    poster = baseAnimeFragment.poster?.posterFragment?.asNetworkModel(),
    kind = baseAnimeFragment.kind.asAnimeKind(),
    status = baseAnimeFragment.status?.asEntryStatus() ?: EntryStatus.None,
    score = baseAnimeFragment.score?.toFloat() ?: 0f,
    episodes = baseAnimeFragment.episodes,
    episodesAired = baseAnimeFragment.episodesAired,
    airedOn = airedOn?.date?.asIncompleteDate(),
    releasedOn = releasedOn?.date?.asIncompleteDate(),
    userRate = userRate
)