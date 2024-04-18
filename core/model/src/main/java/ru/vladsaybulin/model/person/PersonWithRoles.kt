package ru.vladsaybulin.model.person

data class PersonWithRoles(
    val person: Person,
    val englishRoles: List<String>,
    val russianRoles: List<String>
)

fun PersonWithRoles.isMain() = englishRoles.any { mainPersonEnglishRoles.contains(it) }

val mainPersonEnglishRoles = listOf("Director", "Original Creator")