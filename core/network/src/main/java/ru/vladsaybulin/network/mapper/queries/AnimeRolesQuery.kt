package ru.vladsaybulin.network.mapper.queries

import ru.vladsaybulin.core.network.graphql.AnimeRolesQuery
import ru.vladsaybulin.network.models.character.NetworkCharacter
import ru.vladsaybulin.network.models.character.NetworkCharacterWithRole
import ru.vladsaybulin.network.models.common.NetworkImage
import ru.vladsaybulin.network.models.common.NetworkTitleRoles
import ru.vladsaybulin.network.models.person.NetworkPerson
import ru.vladsaybulin.network.models.person.NetworkPersonWithRoles

internal fun AnimeRolesQuery.Anime.asNetworkModel() = NetworkTitleRoles(
    authors = personRoles?.map(AnimeRolesQuery.PersonRole::asNetworkModel),
    characters = characterRoles?.map(AnimeRolesQuery.CharacterRole::asNetworkModel),
)

private fun AnimeRolesQuery.PersonRole.asNetworkModel() = NetworkPersonWithRoles(
    person = NetworkPerson(
        id = person.id,
        name = person.name,
        nameRu = person.russian,
        image = person.poster?.let {
            NetworkImage(
                originalUrl = it.originalUrl,
                previewUrl = it.main2xUrl
            )
        }
    ),
    roles = rolesEn
)

private fun AnimeRolesQuery.CharacterRole.asNetworkModel() = NetworkCharacterWithRole(
    character = NetworkCharacter(
        id = character.id,
        name = character.name,
        nameRu = character.russian,
        image = character.poster?.let { NetworkImage(it.originalUrl, it.main2xUrl) }
    ),
    isMain = rolesEn.contains("Main")
)