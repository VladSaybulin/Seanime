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

import ru.vladsaybulin.core.network.graphql.fragment.CharacterWithRoleFragment
import ru.vladsaybulin.core.network.graphql.fragment.GenreFragment
import ru.vladsaybulin.core.network.graphql.fragment.PersonWithRolesFragment
import ru.vladsaybulin.core.network.graphql.fragment.RelatedEntryFragment
import ru.vladsaybulin.core.network.graphql.fragment.ScoreStatFragment
import ru.vladsaybulin.core.network.graphql.fragment.StatusStatFragment
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.network.mapper.enums.asEntryType
import ru.vladsaybulin.network.mapper.enums.asGenreKind
import ru.vladsaybulin.network.mapper.enums.asRelationType
import ru.vladsaybulin.network.mapper.enums.asUserRateStatus
import ru.vladsaybulin.network.models.character.NetworkCharacterWithRole
import ru.vladsaybulin.network.models.common.NetworkGenre
import ru.vladsaybulin.network.models.common.NetworkStatisticsItem
import ru.vladsaybulin.network.models.person.NetworkPersonWithRoles
import ru.vladsaybulin.network.models.related.NetworkRelated

internal fun GenreFragment.asNetworkModel(): NetworkGenre =
    NetworkGenre(
        id = id,
        name = name,
        russianName = russian,
        titleType = entryType.asEntryType(),
        kind = kind.asGenreKind()
    )

internal fun StatusStatFragment.asNetworkModel(): NetworkStatisticsItem<UserRateStatus> =
    NetworkStatisticsItem(
        values = status.asUserRateStatus(),
        count = count
    )

internal fun ScoreStatFragment.asNetworkModel(): NetworkStatisticsItem<Int> =
    NetworkStatisticsItem(
        values = score,
        count = count
    )

internal fun RelatedEntryFragment.asNetworkModel(): NetworkRelated =
    NetworkRelated(
        anime = anime?.animeFragment?.asNetworkModel(),
        manga = manga?.mangaFragment?.asNetworkModel(),
        relationType = relationKind.asRelationType()
    )

internal fun PersonWithRolesFragment.asNetworkModel(): NetworkPersonWithRoles =
    NetworkPersonWithRoles(
        person = person.personFragment.asNetworkModel(),
        roles = rolesEn
    )

internal fun CharacterWithRoleFragment.asNetworkModel(): NetworkCharacterWithRole =
    NetworkCharacterWithRole(
        character = character.characterFragment.asNetworkModel(),
        isMain = rolesEn.indexOfFirst { it.equals("main", ignoreCase = true) } != -1
    )