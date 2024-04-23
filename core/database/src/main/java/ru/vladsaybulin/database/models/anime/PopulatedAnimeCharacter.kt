package ru.vladsaybulin.database.models.anime

import androidx.room.Embedded
import androidx.room.Relation
import ru.vladsaybulin.database.models.character.CharacterEntity
import ru.vladsaybulin.database.models.character.asExternalModel
import ru.vladsaybulin.model.character.CharacterWithRole

class PopulatedAnimeCharacter(

    @Embedded val animeCharacterEntity: AnimeCharacterEntity,

    @Relation(
        entity = CharacterEntity::class,
        parentColumn = "character_id",
        entityColumn = "id"
    )
    val character: CharacterEntity
)

fun PopulatedAnimeCharacter.asExternalModel() = CharacterWithRole(
    character = character.asExternalModel(),
    isMain = animeCharacterEntity.isMain
)