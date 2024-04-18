package ru.vladsaybulin.network.models.character

data class NetworkCharacterWithRole(
    val character: NetworkCharacter,
    val isMain: Boolean
)