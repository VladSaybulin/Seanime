package ru.vladsaybulin.shikimori.navigation

import androidx.navigation.NavController
import ru.vladsaybulin.core.navigation.SeanimeNavigator
import ru.vladsaybulin.feature.character.navigation.navigateToCharacter
import ru.vladsaybulin.feature.details.navigation.navigateToAnimeDetails
import ru.vladsaybulin.feature.details.navigation.navigateToMangaDetails
import ru.vladsaybulin.feature.search.navigation.navigateToSearchByGenre
import ru.vladsaybulin.feature.search.navigation.navigateToSearchByStudioOrPublisher
import ru.vladsaybulin.feature.search.navigation.navigateToSearchOngoingAnimes
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.genre.GenreKind
import ru.vladsaybulin.model.search.SearchType
import ru.vladsaybulin.model.userrate.UserRateWithEntry

class SeanimeNavigatorImpl(
    private val navController: NavController,
    private val onImageView: (images: List<Image>, index: Int) -> Unit,
    private val onExternalClick: (url: String) -> Unit,
    private val onAuth: () -> Unit,
) : SeanimeNavigator {

    override fun back() {
        navController.navigateUp()
    }

    override fun animeDetails(animeId: Long) {
        navController.navigateToAnimeDetails(animeId)
    }

    override fun mangaDetails(mangaId: Long) {
        navController.navigateToMangaDetails(mangaId)
    }

    override fun characterDetails(characterId: Long) {
        navController.navigateToCharacter(characterId)
    }

    override fun personDetails(personId: Long) {
        //TODO
    }

    override fun searchByGenre(searchType: SearchType, genreKind: GenreKind, genreId: Long) {
        navController.navigateToSearchByGenre(searchType, genreKind, genreId)
    }

    override fun searchByStudioOrPublisher(searchType: SearchType, studioOrPublisherId: Long) {
        navController.navigateToSearchByStudioOrPublisher(searchType, studioOrPublisherId)
    }

    override fun searchOngoingAnimes() {
        navController.navigateToSearchOngoingAnimes()
    }

    override fun userRate(userRateWithEntry: UserRateWithEntry) {
        //TODO
    }

    override fun imageView(images: List<Image>, index: Int) {
        onImageView(images, index)
    }

    override fun requireAuthDialog() {
        //TODO
    }

    override fun externalLink(url: String) {
        onExternalClick(url)
    }

    override fun news() {
        //TODO
    }

    override fun auth() {
        onAuth()
    }
}