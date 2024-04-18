package ru.vladsaybulin.model.character

import ru.vladsaybulin.model.common.Poster

class Character(
    val id: Long,
    val originalName: String,
    val russianName: String?,
    val poster: Poster?
)