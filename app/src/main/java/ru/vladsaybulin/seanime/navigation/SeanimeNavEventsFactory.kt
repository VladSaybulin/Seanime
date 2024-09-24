package ru.vladsaybulin.seanime.navigation

import androidx.navigation.NavController
import androidx.navigation.NavHostController
import ru.vladsaybulin.feature.title.authors.navigation.TitleAuthorsNavEvents
import ru.vladsaybulin.feature.title.authors.navigation.navigateToTitleAuthors
import ru.vladsaybulin.feature.calendar.navigation.CalendarNavEvents
import ru.vladsaybulin.feature.character.navigation.CharacterDetailsNavEvents
import ru.vladsaybulin.feature.character.navigation.navigateToCharacterDetails
import ru.vladsaybulin.feature.title.details.navigation.TitleDetailsNavEvents
import ru.vladsaybulin.feature.title.details.navigation.navigateToTitleDetails
import ru.vladsaybulin.feature.home.navigation.HomeNavEvents
import ru.vladsaybulin.feature.list.navigation.ListNavEvents
import ru.vladsaybulin.feature.search.navigation.SearchNavEvents
import ru.vladsaybulin.feature.search.navigation.navigateToSearchByGenre
import ru.vladsaybulin.feature.search.navigation.navigateToSearchScreenByStatus
import ru.vladsaybulin.feature.search.navigation.navigateToSearchScreenByStudioOrPublisher
import ru.vladsaybulin.feature.title.characters.navigation.TitleCharactersNavEvents
import ru.vladsaybulin.feature.title.characters.navigation.navigateToTitleCharacters
import ru.vladsaybulin.feature.title.related.navigation.TitleRelatedNavEvents
import ru.vladsaybulin.feature.title.related.navigation.navigateToTitleRelated
import ru.vladsaybulin.feature.title.screenshots.navigation.AnimeScreenshotsNavEvents
import ru.vladsaybulin.feature.title.screenshots.navigation.navigateToAnimeScreenshots
import ru.vladsaybulin.feature.title.videos.navigation.AnimeVideosNavEvents
import ru.vladsaybulin.feature.title.videos.navigation.navigateToAnimeVideos
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.search.SearchType
import ru.vladsaybulin.model.userrate.EditableUserRate

class SeanimeNavEventsFactory(
    private val navController: NavHostController,
    private val navigateToUrl: (String) -> Unit,
    private val showUserRateEditor: (EditableUserRate) -> Unit,
    private val runAuthorization: () -> Unit,
    private val showFullscreenImage: (images: List<Image>, initialIndex: Int) -> Unit
) {
    fun createHomeNavEvents() = HomeNavEvents(
        navigateToTitleDetails = navController::navigateToTitleDetails,
        navigateToSearchAnimeOngoing = { navController.navigateToSearchScreenByStatus(SearchType.Anime, EntryStatus.Ongoing) },
        showUserRateEditor = showUserRateEditor
    )

    fun createSearchNavEvents() = SearchNavEvents(
        navigateToAnime = { navController.navigateToTitleDetails(EntryType.Anime, it) },
        navigateToManga = { navController.navigateToTitleDetails(EntryType.Manga, it) }
    )

    fun createListNavEvents() = ListNavEvents(
        navigateToTitleDetails = navController::navigateToTitleDetails,
        showUserRateEditor = showUserRateEditor,
        startAuthorization = runAuthorization,
        navigateUp = navController::navigateUp
    )

    fun createCalendarNavEvents() = CalendarNavEvents(
        navigateToAnimeDetails = { navController.navigateToTitleDetails(EntryType.Anime, it) },
    )

    fun createTitleDetailNavEvents() = TitleDetailsNavEvents(
        navigateToTitleDetails = navController::navigateToTitleDetails,
        navigateToCharacterDetails = navController::navigateToCharacterDetails,
        navigateToPersonDetails = { },
        navigateToUrl = navigateToUrl,
        navigateToTitleAuthors = navController::navigateToTitleAuthors,
        navigateToSearchByGenre = navController::navigateToSearchByGenre,
        navigateToSearchByStudio = navController::navigateToSearchScreenByStudioOrPublisher,
        navigateToSearchByPublisher = navController::navigateToSearchScreenByStudioOrPublisher,
        navigateUp = navController::navigateUp,
        showUserRateEditor = showUserRateEditor,
        showFullScreenImage = showFullscreenImage,
        navigateToTitleRelated = navController::navigateToTitleRelated,
        navigateToTitleCharacters = navController::navigateToTitleCharacters,
        navigateToTitleVideos = navController::navigateToAnimeVideos,
        navigateToTitleScreenshots = navController::navigateToAnimeScreenshots,
        authWithShikimori = runAuthorization
    )

    fun createTitleAuthorsNavEvents() = TitleAuthorsNavEvents(
        navigateToPerson = {  },
        navigateUp = navController::navigateUp
    )

    fun createTitleRelatedNavEVents() = TitleRelatedNavEvents(
        navigateToTitleDetails = navController::navigateToTitleDetails,
        navigateUp = navController::navigateUp
    )

    fun createTitleCharactersNavEvents() = TitleCharactersNavEvents(
        navigateToCharacterDetails = navController::navigateToCharacterDetails,
        navigateUp = navController::navigateUp
    )

    fun createAnimeScreenshotsNavEvents() = AnimeScreenshotsNavEvents(
        showFullscreenImage = showFullscreenImage,
        navigateUp = navController::navigateUp
    )

    fun createAnimeVideosNavEvents() = AnimeVideosNavEvents(
        navigateToVideo = { url, _, _ -> navigateToUrl(url) },
        navigateUp = navController::navigateUp
    )

    fun createCharacterDetailsNavEvents() = CharacterDetailsNavEvents(
        navigateToAnimeDetails = { navController.navigateToTitleDetails(EntryType.Anime, it) },
        navigateToMangaDetails = { navController.navigateToTitleDetails(EntryType.Manga, it) },
        navigateToCharacterDetails = navController::navigateToCharacterDetails,
        navigateToPersonDetails = { },
        navigateToUrl = navigateToUrl,
        navigateUp = navController::navigateUp
    )
}

private fun NavController.navigateToAnimeScreenshots(titleType: EntryType, titleId: Long) {
    check(titleType == EntryType.Anime)
    navigateToAnimeScreenshots(titleId)
}

private fun NavController.navigateToAnimeVideos(titleType: EntryType, titleId: Long) {
    check(titleType == EntryType.Anime)
    navigateToAnimeVideos(titleId)
}