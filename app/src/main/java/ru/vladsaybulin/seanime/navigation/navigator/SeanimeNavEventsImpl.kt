package ru.vladsaybulin.seanime.navigation.navigator

import androidx.navigation.NavController
import ru.vladsaybulin.feature.authors.navigation.TitleAuthorsArgs
import ru.vladsaybulin.feature.character.navigation.CharacterDetailsArgs
import ru.vladsaybulin.feature.details.navigation.TitleDetailsArgs
import ru.vladsaybulin.feature.search.navigation.SearchArgs
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.genre.GenreKind
import ru.vladsaybulin.model.search.SearchType

class SeanimeNavEventsImpl(
    private val navController: NavController,
    private val openUrl: (String) -> Unit,
    private val onAuth: () -> Unit,
    private val openUserRateEditor: () -> Unit,
    private val openFullscreenImage: (images: List<Image>, startImageIndex: Int) -> Unit,
    private val provider: RouteProvider
) : SeanimeNavEvents {

    override fun navigateToPersonDetails(personId: Long) {
        TODO("Not yet implemented")
    }

    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun navigateToCharacterDetails(characterId: Long) {
        navController.navigate(
            route = provider.characterDetailsRoute(
                CharacterDetailsArgs(characterId)
            )
        )
    }

    override fun navigateToUrl(url: String) {
        openUrl(url)
    }

    override fun navigateToTitleAuthors(titleType: EntryType, titleId: Long) {
        provider.titleAuthorsScreenRoute(TitleAuthorsArgs(titleType, titleId))
    }

    override fun navigateToTitleDetails(titleType: EntryType, titleId: Long) {
        navController.navigate(
            provider.titleDetailsScreenRoute(
                TitleDetailsArgs(
                    titleType = titleType,
                    titleId = titleId
                )
            )
        )
    }

    override fun showRequestAuthorization() {
        TODO("Not yet implemented")
    }

    override fun startAuthorization() {
        onAuth()
    }

    override fun navigateToSearchByGenre(
        searchType: SearchType,
        genreKind: GenreKind,
        genreId: Long
    ) {
        provider.searchScreenRoute(
            SearchArgs.searchByGenreArgs(
                searchType = searchType,
                genreKind = genreKind,
                genreId = genreId
            )
        )
    }

    override fun navigateToSearchByStudio(searchType: SearchType, studioId: Long) {
        provider.searchScreenRoute(SearchArgs.searchAnimeByStudio(studioId))
    }

    override fun navigateToSearchAnimeOngoing() {
        provider.searchScreenRoute(SearchArgs.searchAnimeOngoing())
    }

    override fun navigateToSearchMangaOrRanobeByPublisher(searchType: SearchType, publisherId: Long) {
        provider.searchScreenRoute(
            SearchArgs.searchMangaOrRanobeByPublisher(
                isManga = searchType == SearchType.Manga,
                publisherId = publisherId
            )
        )
    }

    override fun showUserRateEditor() {
        openUserRateEditor()
    }

    override fun showFullscreenImage(images: List<Image>, startImageIndex: Int) {
        openFullscreenImage(images, startImageIndex)
    }

    override fun navigateToAllNewsTopics() {
        TODO("Not yet implemented")
    }


}