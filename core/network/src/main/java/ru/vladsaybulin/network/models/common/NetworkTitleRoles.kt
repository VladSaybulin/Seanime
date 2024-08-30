package ru.vladsaybulin.network.models.common

import ru.vladsaybulin.network.models.character.NetworkCharacterWithRole
import ru.vladsaybulin.network.models.person.NetworkPersonWithRoles

class NetworkTitleRoles(
    val authors: List<NetworkPersonWithRoles>?,
    val characters: List<NetworkCharacterWithRole>?
)