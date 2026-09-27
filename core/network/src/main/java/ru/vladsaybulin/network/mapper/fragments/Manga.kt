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

import ru.vladsaybulin.core.network.graphql.fragment.MangaDetailsFragment
import ru.vladsaybulin.core.network.graphql.fragment.MangaFragment
import ru.vladsaybulin.core.network.graphql.fragment.MangaWithLocalDateFragment
import ru.vladsaybulin.core.network.graphql.fragment.PublisherFragment
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.network.mapper.enums.asEntryStatus
import ru.vladsaybulin.network.mapper.enums.asMangaKind
import ru.vladsaybulin.network.models.manga.NetworkManga
import ru.vladsaybulin.network.models.manga.NetworkMangaDetails
import ru.vladsaybulin.network.models.manga.NetworkPublisher
import ru.vladsaybulin.network.models.userrate.NetworkUserRate

internal fun MangaFragment.asNetworkModel() = NetworkManga(
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
    releasedOn = releasedOn?.incompleteDateFragment?.asNetworkModel()
)

internal fun MangaWithLocalDateFragment.asNetworkModel() = NetworkManga(
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
    releasedOn = releasedOn?.date?.asIncompleteDate()
)

internal fun MangaDetailsFragment.asNetworkModel(userRate: NetworkUserRate? = null) = NetworkMangaDetails(
    id = id,
    nameEn = english,
    nameJp = japanese,
    alternativeName = synonyms.joinToString(separator = ", ").takeIf { it.isNotBlank() },
    licenseNameRu = licenseNameRu,
    descriptionHtml = descriptionHtml?.takeIf { it.isNotBlank() },
    descriptionSource = descriptionSource?.takeIf { it.isNotBlank() },
    genres = genres?.map { it.genreFragment.asNetworkModel() },
    scoreStats = scoresStats?.map { it.scoreStatFragment.asNetworkModel() },
    userRateStatusStats = statusesStats?.map { it.statusStatFragment.asNetworkModel() },
    publishers = publishers.map { it.publisherFragment.asNetworkModel() },
    related = related?.map { it.relatedEntryFragment.asNetworkModel() },
    userRate = userRate
)

private fun PublisherFragment.asNetworkModel() = NetworkPublisher(
    id = id,
    name = name
)