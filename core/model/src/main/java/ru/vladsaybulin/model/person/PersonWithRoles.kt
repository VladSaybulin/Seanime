package ru.vladsaybulin.model.person

data class PersonWithRoles(
    val person: Person,
    val roles: List<String>,
    val isMain: Boolean
)