package ru.vladsaybulin.network.mapper.enums

import ru.vladsaybulin.core.network.graphql.type.GenreKindEnum
import ru.vladsaybulin.model.GenreKind

fun GenreKindEnum.asGenreKind() = when (this) {
    GenreKindEnum.demographic -> GenreKind.Demographic
    GenreKindEnum.theme -> GenreKind.Theme
    else -> GenreKind.Genre
}