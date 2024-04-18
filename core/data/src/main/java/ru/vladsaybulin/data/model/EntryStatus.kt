package ru.vladsaybulin.data.model

import ru.vladsaybulin.core.network.graphql.type.AnimeStatusEnum
import ru.vladsaybulin.core.network.graphql.type.MangaStatusEnum
import ru.vladsaybulin.model.common.EntryStatus

fun AnimeStatusEnum?.asEntryStatus() = when (this) {
    AnimeStatusEnum.anons -> EntryStatus.Anons
    AnimeStatusEnum.ongoing -> EntryStatus.Ongoing
    AnimeStatusEnum.released -> EntryStatus.Released
    else -> EntryStatus.None
}

fun MangaStatusEnum?.asEntryStatus() = when (this) {
    MangaStatusEnum.anons -> EntryStatus.Anons
    MangaStatusEnum.ongoing -> EntryStatus.Ongoing
    MangaStatusEnum.released -> EntryStatus.Released
    MangaStatusEnum.discontinued -> EntryStatus.Discontinued
    MangaStatusEnum.paused -> EntryStatus.Paused
    else -> EntryStatus.None
}