package ru.vladsaybulin.network.mapper.fragments

import ru.vladsaybulin.core.network.graphql.fragment.CharacterFragment
import ru.vladsaybulin.network.models.character.NetworkCharacter

internal fun CharacterFragment.asNetworkModel() = NetworkCharacter(
    id = id,
    name = name,
    nameRu = russian,
    image = poster?.posterFragment?.asNetworkModel()
)