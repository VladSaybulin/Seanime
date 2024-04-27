package ru.vladsaybulin.core.navigation.args

import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.manga.Manga

data class EntryDetailsArgs(
    val entryType: EntryType,
    val entryId: Long
)

fun Anime.asEntryDetailsArgs() = EntryDetailsArgs(EntryType.Anime, id)

fun Manga.asEntryDetailsArgs() = EntryDetailsArgs(EntryType.Manga, id)