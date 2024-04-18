package ru.vladsaybulin.model.character

import ru.vladsaybulin.model.common.Image

class Character(
    val id: Long,
    val originalName: String,
    val russianName: String?,
    val poster: Image?
)