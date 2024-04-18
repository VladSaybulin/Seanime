package ru.vladsaybulin.model.person

import ru.vladsaybulin.model.common.Image

data class Person(
    val id: Long,
    val originalName: String,
    val russianName: String?,
    val poster: Image?
)