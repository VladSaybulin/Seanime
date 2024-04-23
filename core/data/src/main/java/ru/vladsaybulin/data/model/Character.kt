package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.character.CharacterEntity
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

fun NetworkCharacter.asEntity() = CharacterEntity(
    id = id,
    name = name,
    nameRu = nameRu,
    image = image?.asPOJO()
)