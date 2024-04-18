package ru.vladsaybulin.data.model

import ru.vladsaybulin.model.character.Character
import ru.vladsaybulin.model.character.CharacterWithRole
import ru.vladsaybulin.network.models.character.NetworkCharacter
import ru.vladsaybulin.network.models.character.NetworkCharacterWithRole

fun NetworkCharacterWithRole.asExternalModel() = CharacterWithRole(
    character = character.asExternalModel(),
    isMain = isMain
)

fun NetworkCharacter.asExternalModel() = Character(
    id = id,
    originalName = name,
    russianName = nameRu,
    poster = image?.asExternalModel()
)