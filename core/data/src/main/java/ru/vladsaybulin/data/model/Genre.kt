package ru.vladsaybulin.data.model

import ru.vladsaybulin.core.network.graphql.type.GenreKindEnum
import ru.vladsaybulin.model.GenreKind

fun GenreKindEnum.asGenreKind() = when (this) {
    GenreKindEnum.genre -> GenreKind.Genre
    GenreKindEnum.theme -> GenreKind.Theme
    GenreKindEnum.demographic -> GenreKind.Demographic
    GenreKindEnum.UNKNOWN__ -> error("Unknown GenreKind")
}