package ru.vladsaybulin.core.navigation

import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.genre.GenreKind
import ru.vladsaybulin.model.search.SearchType
import ru.vladsaybulin.model.userrate.UserRateWithEntry

/**
 * Used for @Preview composable
 */
object IdleSeanimeNavigator : SeanimeNavigator {

    override fun back() {
    }

    override fun animeDetails(animeId: Long) {
    }

    override fun mangaDetails(mangaId: Long) {
    }

    override fun characterDetails(characterId: Long) {
    }

    override fun personDetails(personId: Long) {
    }

    override fun searchByGenre(searchType: SearchType, genreKind: GenreKind, genreId: Long) {
    }

    override fun searchByStudioOrPublisher(searchType: SearchType, studioOrPublisherId: Long) {
    }

    override fun searchOngoingAnimes() {
    }

    override fun userRate(userRateWithEntry: UserRateWithEntry) {
    }

    override fun imageView(images: List<Image>, index: Int) {
    }

    override fun requireAuthDialog() {
    }

    override fun externalLink(url: String) {
    }

    override fun news() {
    }

    override fun auth() {
    }

    override fun authors(entryType: EntryType, entryId: Long) {
    }
}