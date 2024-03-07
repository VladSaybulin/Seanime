package ru.vladsaybulin.model

class Character(
    val id: Long,
    val originalName: String,
    val russianName: String?,
    val poster: Poster?
)