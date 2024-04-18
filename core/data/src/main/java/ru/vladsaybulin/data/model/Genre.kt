package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.genre.GenreEntity
import ru.vladsaybulin.model.genre.Genre
import ru.vladsaybulin.network.models.NetworkGenre

fun NetworkGenre.asPOJO() = GenreEntity(
    id = id,
    name = name,
    nameRu = russianName,
    entryType = entryType,
    kind = kind
)

fun NetworkGenre.asExternalModel() = Genre(
    id = id,
    englishName = name,
    russianName = russianName,
    entryType = entryType,
    kind = kind
)