package ru.vladsaybulin.data.model

import ru.vladsaybulin.core.network.graphql.type.GenreKindEnum
import ru.vladsaybulin.database.models.genre.GenreEntity
import ru.vladsaybulin.model.genre.GenreKind
import ru.vladsaybulin.network.models.NetworkGenre

fun GenreKindEnum.asGenreKind() = when (this) {
    GenreKindEnum.genre -> GenreKind.Genre
    GenreKindEnum.theme -> GenreKind.Theme
    GenreKindEnum.demographic -> GenreKind.Demographic
    GenreKindEnum.UNKNOWN__ -> error("Unknown GenreKind")
}

fun NetworkGenre.asEntity() = GenreEntity(
    id = id,
    name = name,
    nameRu = russianName,
    entryType = entryType,
    kind = kind
)