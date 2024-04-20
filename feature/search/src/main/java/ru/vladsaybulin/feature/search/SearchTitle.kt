package ru.vladsaybulin.feature.search

import ru.vladsaybulin.model.common.EntryStatus

sealed class SearchTitle {

    data object Search : SearchTitle()

    data class Status(val entryStatus: EntryStatus) : SearchTitle()

    data class Studio(val studioName: String) : SearchTitle()

    data class Publisher(val publisherName: String) : SearchTitle()

    data class Genre(val genreName: String) : SearchTitle()

    data class Demographic(val demographicName: String) : SearchTitle()

    data class Theme(val themeName: String) : SearchTitle()

}