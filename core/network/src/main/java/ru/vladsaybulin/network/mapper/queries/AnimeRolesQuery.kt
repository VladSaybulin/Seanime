package ru.vladsaybulin.network.mapper.queries

import ru.vladsaybulin.core.network.graphql.AnimeRolesQuery
import ru.vladsaybulin.network.mapper.fragments.asNetworkModel
import ru.vladsaybulin.network.models.character.NetworkCharacterWithRole
import ru.vladsaybulin.network.models.common.NetworkTitleRoles
import ru.vladsaybulin.network.models.person.NetworkPersonWithRoles

internal fun AnimeRolesQuery.Anime.asNetworkModel() = NetworkTitleRoles(
    authors = personRoles?.map(AnimeRolesQuery.PersonRole::asNetworkModel),
    characters = characterRoles?.map(AnimeRolesQuery.CharacterRole::asNetworkModel),
)

private fun AnimeRolesQuery.PersonRole.asNetworkModel() = NetworkPersonWithRoles(
    person = person.personFragment.asNetworkModel(),
    roles = rolesEn
)

private fun AnimeRolesQuery.CharacterRole.asNetworkModel() = NetworkCharacterWithRole(
    character = character.characterFragment.asNetworkModel(),
    isMain = rolesEn.contains("Main")
)