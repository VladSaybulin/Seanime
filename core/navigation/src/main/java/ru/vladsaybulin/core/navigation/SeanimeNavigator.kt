package ru.vladsaybulin.core.navigation

import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.Studio
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.genre.Genre
import ru.vladsaybulin.model.genre.GenreKind
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.manga.Publisher
import ru.vladsaybulin.model.search.SearchType
import ru.vladsaybulin.model.userrate.UserRateWithEntry

interface SeanimeNavigator {

    fun back()

    fun animeDetails(animeId: Long)

    fun mangaDetails(mangaId: Long)

    fun characterDetails(characterId: Long)

    fun personDetails(personId: Long)

    fun searchByGenre(searchType: SearchType, genreKind: GenreKind, genreId: Long)

    fun searchByStudioOrPublisher(searchType: SearchType, studioOrPublisherId: Long)

    fun searchOngoingAnimes()

    fun userRate(userRateWithEntry: UserRateWithEntry)

    fun imageView(images: List<Image>, index: Int)

    fun requireAuthDialog()

    fun externalLink(url: String)

    fun news()

    fun auth()

    fun authors(entryType: EntryType, entryId: Long)
}

fun SeanimeNavigator.animeDetails(anime: Anime) {
    animeDetails(anime.id)
}

fun SeanimeNavigator.mangaDetails(manga: Manga) {
    mangaDetails(manga.id)
}

fun SeanimeNavigator.searchByGenre(searchType: SearchType, genre: Genre) {
    check(searchType != SearchType.Anime || genre.entryType == EntryType.Anime)
    check((searchType != SearchType.Manga && searchType != SearchType.Ranobe) || genre.entryType == EntryType.Manga)

    searchByGenre(searchType, genre.kind, genre.id)
}

fun SeanimeNavigator.searchByStudio(studio: Studio) {
    searchByStudioOrPublisher(SearchType.Anime, studio.id)
}

fun SeanimeNavigator.searchByPublisher(searchType: SearchType, publisher: Publisher) {
    check(searchType == SearchType.Manga || searchType == SearchType.Ranobe)
    searchByStudioOrPublisher(searchType, publisher.id)
}