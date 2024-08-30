package ru.vladsaybulin.feature.details.navigation

import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.genre.GenreKind
import ru.vladsaybulin.model.search.SearchType
import java.security.KeyStore.Entry

data class TitleDetailsNavEvents(
    val navigateToTitleDetails: (EntryType, Long) -> Unit,
    val navigateToCharacterDetails: (Long) -> Unit,
    val navigateToPersonDetails: (Long) -> Unit,
    val navigateToUrl: (String) -> Unit,
    val navigateToTitleAuthors: (EntryType, Long) -> Unit,
    val navigateToTitleRelated: (EntryType, Long) -> Unit,
    val navigateToTitleCharacters: (EntryType, Long) -> Unit,
    val navigateToTitleScreenshots: (EntryType, Long) -> Unit,
    val navigateToTitleVideos: (EntryType, Long) -> Unit,
    val navigateToTitleSimilar: (EntryType, Long) -> Unit,
    val navigateToAuthorization: () -> Unit,
    val navigateToSearchByGenre: (type: SearchType, kind: GenreKind, genreId: Long) -> Unit,
    val navigateToSearchByStudio: (SearchType, studioId: Long) -> Unit,
    val navigateToSearchByPublisher: (SearchType, publisherId: Long) -> Unit,
    val navigateUp: () -> Unit,
    val showUserRateEditor: (/* TODO EditableUserRate? */) -> Unit,
    val showFullScreenImage: (allImages: List<Image>, initialImageIndex: Int) -> Unit
)

/**
 * TitleDetailsNavEvents used for preview
 */
internal val IdleTitleDetailsNavEvents = TitleDetailsNavEvents(
    navigateToTitleDetails = { _, _ -> },
    navigateToCharacterDetails = { },
    navigateToPersonDetails = { },
    navigateToUrl = { },
    navigateToTitleAuthors = { _, _ -> },
    navigateToTitleRelated = { _, _ -> },
    navigateToTitleCharacters = { _, _ -> },
    navigateToTitleScreenshots = { _, _ -> },
    navigateToTitleVideos = { _, _ -> },
    navigateToTitleSimilar = { _, _ -> },
    navigateToAuthorization = { },
    navigateToSearchByGenre = { _, _, _ -> },
    navigateToSearchByStudio = { _, _ -> },
    navigateToSearchByPublisher = { _, _ -> },
    navigateUp = { },
    showUserRateEditor = { },
    showFullScreenImage = { _, _ -> }
)