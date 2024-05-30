package ru.vladsaybulin.network.models.person

data class NetworkPersonWithRoles(
    val person: NetworkPerson,
    val roles: List<String>
)