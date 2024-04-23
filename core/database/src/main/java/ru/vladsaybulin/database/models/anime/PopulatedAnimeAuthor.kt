package ru.vladsaybulin.database.models.anime

import androidx.room.Embedded
import androidx.room.Relation
import ru.vladsaybulin.database.models.person.PersonEntity
import ru.vladsaybulin.database.models.person.asExternalModel
import ru.vladsaybulin.model.person.PersonWithRoles

data class PopulatedAnimeAuthor(

    @Embedded val animePersonRolesEntity: AnimePersonRolesEntity,

    @Relation(
        entity = PersonEntity::class,
        parentColumn = "person_id",
        entityColumn = "id"
    )
    val person: PersonEntity
)

fun PopulatedAnimeAuthor.asExternalModel() = PersonWithRoles(
    person = person.asExternalModel(),
    englishRoles = animePersonRolesEntity.rolesEn,
    russianRoles = animePersonRolesEntity.rolesRu
)