package ru.vladsaybulin.database.models.manga

import androidx.room.Embedded
import androidx.room.Relation
import ru.vladsaybulin.database.models.character.CharacterEntity
import ru.vladsaybulin.database.models.character.asExternalModel
import ru.vladsaybulin.model.character.CharacterWithRole

data class PopulatedMangaCharacter(
    @Embedded val mangaCharacterEntity: MangaCharacterEntity,

    @Relation(
        entity = CharacterEntity::class,
        parentColumn = "character_id",
        entityColumn = "id"
    )
    val character: CharacterEntity
)

fun PopulatedMangaCharacter.asExternalModel() = CharacterWithRole(
    character = character.asExternalModel(),
    isMain = mangaCharacterEntity.isMain
)