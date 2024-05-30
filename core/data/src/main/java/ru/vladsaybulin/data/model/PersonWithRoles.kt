package ru.vladsaybulin.data.model


fun List<String>.isMainPersonRoles() = any { it in MainRoles }

private val MainRoles = listOf(
    "Director",
    "Original Creator",
    "Story",
    "Art",
    "Story & Art"
)