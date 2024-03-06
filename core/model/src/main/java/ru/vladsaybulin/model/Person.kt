package ru.vladsaybulin.model

class Person(
    val id: Long,
    val originalName: String,
    val russianName: String,
    val poster: Poster?
)