package ru.vladsaybulin.database.models.manga

import androidx.room.Embedded
import androidx.room.Relation
import ru.vladsaybulin.database.models.person.PersonEntity
import ru.vladsaybulin.database.models.person.asExternalModel
import ru.vladsaybulin.model.person.PersonWithRoles

data class PopulatedMangaAuthor(
    @Embedded val mangaPersonRolesEntity: MangaPersonRolesEntity,

    @Relation(
        entity = PersonEntity::class,
        parentColumn = "person_id",
        entityColumn = "id"
    )
    val person: PersonEntity
)

fun PopulatedMangaAuthor.asExternalModel() = PersonWithRoles(
    person = person.asExternalModel(),
    englishRoles = mangaPersonRolesEntity.rolesEn,
    russianRoles = mangaPersonRolesEntity.rolesRu
)