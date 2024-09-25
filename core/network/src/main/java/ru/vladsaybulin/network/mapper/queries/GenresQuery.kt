package ru.vladsaybulin.network.mapper.queries

import ru.vladsaybulin.core.network.graphql.GenresQuery
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.network.mapper.enums.asGenreKind
import ru.vladsaybulin.network.models.common.NetworkGenre

internal fun GenresQuery.Genre.asNetworkModel(entryType: EntryType) = NetworkGenre(
    id = id,
    name = name,
    russianName = russian,
    kind = kind.asGenreKind(),
    entryType = entryType
)