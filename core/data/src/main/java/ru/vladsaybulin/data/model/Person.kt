package ru.vladsaybulin.data.model

import ru.vladsaybulin.model.person.Person
import ru.vladsaybulin.model.person.PersonWithRoles
import ru.vladsaybulin.network.models.person.NetworkPerson
import ru.vladsaybulin.network.models.person.NetworkPersonWithRoles

fun NetworkPersonWithRoles.asExternalModel() = PersonWithRoles(
    person = person.asExternalModel(),
    englishRoles = roles,
    russianRoles = russianRoles
)

fun NetworkPerson.asExternalModel() = Person(
    id = id,
    originalName = name,
    russianName = nameRu,
    poster = image?.asExternalModel()
)