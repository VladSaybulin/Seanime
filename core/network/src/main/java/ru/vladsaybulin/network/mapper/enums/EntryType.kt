package ru.vladsaybulin.network.mapper.enums

import ru.vladsaybulin.core.network.graphql.type.GenreEntryTypeEnum
import ru.vladsaybulin.model.common.EntryType

fun EntryType.asGenreEntryTypeEnum() = when (this) {
    EntryType.Anime -> GenreEntryTypeEnum.Anime
    EntryType.Manga -> GenreEntryTypeEnum.Manga
}