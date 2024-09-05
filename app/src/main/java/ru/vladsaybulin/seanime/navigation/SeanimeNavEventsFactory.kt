package ru.vladsaybulin.seanime.navigation

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
import ru.vladsaybulin.feature.title.related.navigation.TitleRelatedNavEvents
import ru.vladsaybulin.feature.title.related.navigation.navigateToTitleRelated
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.search.SearchType
import ru.vladsaybulin.model.userrate.EditableUserRate

class SeanimeNavEventsFactory(
    private val navController: NavHostController,
    private val navigateToUrl: (String) -> Unit,
    private val showUserRateEditor: (EditableUserRate) -> Unit,
    private val showRequestAuthorization: () -> Unit,
    private val runAuthorization: () -> Unit,
    private val showFullscreenImage: (images: List<Image>, initialIndex: Int) -> Unit
) {
    fun createHomeNavEvents() = HomeNavEvents(
        navigateToTitleDetails = navController::navigateToTitleDetails,
        navigateToSearchAnimeOngoing = { navController.navigateToSearchScreenByStatus(SearchType.Anime, EntryStatus.Ongoing) },
        navigateToUrl = navigateToUrl,
        navigateToAllNewsTopics = {  },
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
        navigateToAuthorization = showRequestAuthorization,
        navigateToSearchByGenre = navController::navigateToSearchByGenre,
        navigateToSearchByStudio = navController::navigateToSearchScreenByStudioOrPublisher,
        navigateToSearchByPublisher = navController::navigateToSearchScreenByStudioOrPublisher,
        navigateUp = navController::navigateUp,
        showUserRateEditor = showUserRateEditor,
        showFullScreenImage = showFullscreenImage,
        navigateToTitleRelated = navController::navigateToTitleRelated,
        navigateToTitleVideos = {_, _ -> },
        navigateToTitleCharacters = {_, _ -> },
        navigateToTitleScreenshots = {_, _ -> }
    )

    fun createTitleAuthorsNavEvents() = TitleAuthorsNavEvents(
        navigateToPerson = {  },
        navigateUp = navController::navigateUp
    )

    fun createTitleRelatedNavEVents() = TitleRelatedNavEvents(
        navigateToTitleDetails = navController::navigateToTitleDetails,
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