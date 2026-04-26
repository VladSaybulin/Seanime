/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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