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

package ru.vladsaybulin.network.mapper.queries

import ru.vladsaybulin.core.network.graphql.AnimeBriefQuery
import ru.vladsaybulin.core.network.graphql.AnimeDetailsQuery
import ru.vladsaybulin.core.network.graphql.AnimeRolesQuery
import ru.vladsaybulin.network.mapper.fragments.asNetworkModel
import ru.vladsaybulin.network.models.anime.NetworkAnime
import ru.vladsaybulin.network.models.anime.NetworkAnimeDetails
import ru.vladsaybulin.network.models.common.NetworkTitleRoles

internal fun AnimeBriefQuery.Anime.asNetworkModel(): NetworkAnime {
    return animeFragment.asNetworkModel()
}

internal fun AnimeDetailsQuery.Anime.asNetworkModel(): NetworkAnimeDetails {
    val networkUserRate = userRate?.userRateFragment?.asNetworkModel()
    return animeDetailsFragment.asNetworkModel(userRate = networkUserRate)
}

internal fun AnimeRolesQuery.Anime.asNetworkModel(): NetworkTitleRoles = with(animeRolesFragment) {
    return NetworkTitleRoles(
        characters = characterRoles?.map { it.characterWithRoleFragment.asNetworkModel() },
        authors = personRoles?.map { it.personWithRolesFragment.asNetworkModel() }
    )
}