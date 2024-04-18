package ru.vladsaybulin.model.person

import ru.vladsaybulin.model.common.Poster

data class Person(
    val id: Long,
    val originalName: String,
    val russianName: String?,
    val poster: Poster?
)