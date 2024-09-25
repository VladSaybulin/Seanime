package ru.vladsaybulin.network.mapper.fragments

import ru.vladsaybulin.core.network.graphql.fragment.PersonFragment
import ru.vladsaybulin.network.models.person.NetworkPerson

internal fun PersonFragment.asNetworkModel() = NetworkPerson(
    id = id,
    name = name,
    nameRu = russian,
    image = poster?.posterFragment?.asNetworkModel()
)