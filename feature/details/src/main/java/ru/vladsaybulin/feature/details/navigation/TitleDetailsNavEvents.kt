package ru.vladsaybulin.feature.details.navigation

import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.genre.GenreKind
import ru.vladsaybulin.model.search.SearchType

data class TitleDetailsNavEvents(
    val navigateToTitleDetails: (EntryType, Long) -> Unit,
    val navigateToCharacterDetails: (Long) -> Unit,
    val navigateToPersonDetails: (Long) -> Unit,
    val navigateToUrl: (String) -> Unit,
    val navigateToTitleAuthors: (EntryType, Long) -> Unit,
    val navigateToAuthorization: () -> Unit,
    val navigateToSearchByGenre: (type: SearchType, kind: GenreKind, genreId: Long) -> Unit,
    val navigateToSearchAnimeByStudio: (studioId: Long) -> Unit,
    val navigateToSearchMangaOrRanobeByPublisher: (isManga: Boolean, publisherId: Long) -> Unit,
    val navigateUp: () -> Unit,
    val showUserRateEditor: (/* TODO EditableUserRate? */) -> Unit,
    val showFullScreenImage: (allImages: List<Image>, initialImageIndex: Int) -> Unit
)
