package ru.vladsaybulin.feature.search

import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.genre.GenreKind

sealed class SearchTitle {

    data object Search : SearchTitle()

    data class Status(val entryStatus: EntryStatus) : SearchTitle()

    data class Studio(val studioName: String) : SearchTitle()

    data class Publisher(val publisherName: String) : SearchTitle()

    data class Genre(val genreName: String, val genreKind: GenreKind) : SearchTitle()
}