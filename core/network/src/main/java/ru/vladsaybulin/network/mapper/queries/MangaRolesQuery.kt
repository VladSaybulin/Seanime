package ru.vladsaybulin.network.mapper.queries

import ru.vladsaybulin.core.network.graphql.MangaRolesQuery
import ru.vladsaybulin.network.mapper.fragments.asNetworkModel
import ru.vladsaybulin.network.models.character.NetworkCharacterWithRole
import ru.vladsaybulin.network.models.common.NetworkTitleRoles
import ru.vladsaybulin.network.models.person.NetworkPersonWithRoles

internal fun MangaRolesQuery.Manga.asNetworkModel() = NetworkTitleRoles(
    authors = personRoles?.map(MangaRolesQuery.PersonRole::asNetworkModel),
    characters = characterRoles?.map(MangaRolesQuery.CharacterRole::asNetworkModel),
)

private fun MangaRolesQuery.PersonRole.asNetworkModel() = NetworkPersonWithRoles(
    person = person.personFragment.asNetworkModel(),
    roles = rolesEn
)

private fun MangaRolesQuery.CharacterRole.asNetworkModel() = NetworkCharacterWithRole(
    character = character.characterFragment.asNetworkModel(),
    isMain = rolesEn.contains("Main")
)

