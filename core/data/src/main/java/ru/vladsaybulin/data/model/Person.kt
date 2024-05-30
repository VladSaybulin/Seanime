package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.person.PersonEntity
import ru.vladsaybulin.network.models.person.NetworkPerson

fun NetworkPerson.asEntity() = PersonEntity(
    id = id,
    name = name,
    nameRu = nameRu,
    image = image?.asPOJO()
)
