package ru.vladsaybulin.seanime.navigation.navigator

import ru.vladsaybulin.feature.authors.navigation.TitleAuthorsNavEvents
import ru.vladsaybulin.feature.calendar.navigation.CalendarNavEvents
import ru.vladsaybulin.feature.character.navigation.CharacterDetailsNavEvents
import ru.vladsaybulin.feature.details.navigation.TitleDetailsNavEvents
import ru.vladsaybulin.feature.home.navigation.HomeNavEvents
import ru.vladsaybulin.feature.list.navigation.ListNavEvents
import ru.vladsaybulin.feature.search.navigation.SearchNavEvents
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.genre.GenreKind
import ru.vladsaybulin.model.search.SearchType

interface SeanimeNavEvents {

    fun navigateToPersonDetails(personId: Long)

    fun navigateUp()

    fun navigateToTitleDetails(titleType: EntryType, titleId: Long)

    fun navigateToCharacterDetails(characterId: Long)

    fun navigateToUrl(url: String)

    fun navigateToTitleAuthors(titleType: EntryType, titleId: Long)

    fun showRequestAuthorization()

    fun startAuthorization()

    fun navigateToSearchByGenre(searchType: SearchType, genreKind: GenreKind, genreId: Long)

    fun navigateToSearchAnimeByStudio(studioId: Long)

    fun navigateToSearchAnimeOngoing()

    fun navigateToSearchMangaOrRanobeByPublisher(isManga: Boolean, publisherId: Long)

    fun showUserRateEditor()

    fun showFullscreenImage(images: List<Image>, startImageIndex: Int)

    fun navigateToAllNewsTopics()

}

private fun SeanimeNavEvents.navigateToAnimeDetails(animeId: Long) {
    navigateToTitleDetails(EntryType.Anime, animeId)
}

private fun SeanimeNavEvents.navigateToMangaDetails(mangaId: Long) {
    navigateToTitleDetails(EntryType.Manga, mangaId)
}

fun SeanimeNavEvents.toCalendarNavEvents() = CalendarNavEvents(
    navigateToAnimeDetails = this::navigateToAnimeDetails
)

fun SeanimeNavEvents.toCharacterDetailsNavEvents() = CharacterDetailsNavEvents(
    navigateToAnimeDetails = this::navigateToAnimeDetails,
    navigateToMangaDetails = this::navigateToMangaDetails,
    navigateToCharacterDetails = this::navigateToCharacterDetails,
    navigateToPersonDetails = this::navigateToPersonDetails,
    navigateToUrl = this::navigateToUrl,
    navigateUp = this::navigateUp
)

fun SeanimeNavEvents.toHomeNavEvents() = HomeNavEvents(
    navigateToTitleDetails = this::navigateToTitleDetails,
    navigateToSearchAnimeOngoing = this::navigateToSearchAnimeOngoing,
    navigateToUrl = this::navigateToUrl,
    navigateToAllNewsTopics = this::navigateToAllNewsTopics,
    showUserRateEditor = this::showUserRateEditor
)

fun SeanimeNavEvents.toListNavHost() = ListNavEvents(
    navigateToTitleDetails = this::navigateToTitleDetails,
    showUserRateEditor = this::showUserRateEditor,
    startAuthorization = this::startAuthorization,
    navigateUp = this::navigateUp
)

fun SeanimeNavEvents.toSearchNavEvents() = SearchNavEvents(
    navigateToAnime = this::navigateToAnimeDetails,
    navigateToManga = this::navigateToMangaDetails
)

fun SeanimeNavEvents.toTitleAuthorsNavEvents() = TitleAuthorsNavEvents(
    navigateToPerson = this::navigateToPersonDetails,
    navigateUp = this::navigateUp
)

fun SeanimeNavEvents.toTitleDetailsNavEvents() = TitleDetailsNavEvents(
    navigateToTitleDetails = this::navigateToTitleDetails,
    navigateToCharacterDetails = this::navigateToCharacterDetails,
    navigateToPersonDetails = this::navigateToPersonDetails,
    navigateToUrl = this::navigateToUrl,
    navigateToTitleAuthors = this::navigateToTitleAuthors,
    navigateToAuthorization = this::showRequestAuthorization,
    navigateToSearchByGenre = this::navigateToSearchByGenre,
    navigateToSearchAnimeByStudio = this::navigateToSearchAnimeByStudio,
    navigateToSearchMangaOrRanobeByPublisher = this::navigateToSearchMangaOrRanobeByPublisher,
    navigateUp = this::navigateUp,
    showUserRateEditor = this::showUserRateEditor,
    showFullScreenImage = this::showFullscreenImage
)