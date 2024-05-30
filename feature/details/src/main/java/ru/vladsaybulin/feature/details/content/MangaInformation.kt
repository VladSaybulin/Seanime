package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.ImmutableList
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.ui.entry.EntryInfoStatusAndDatesText
import ru.vladsaybulin.core.ui.manga.MangaInfoKindAndChaptersAndVolumesText
import ru.vladsaybulin.feature.details.DetailsUiState
import ru.vladsaybulin.feature.details.R
import ru.vladsaybulin.model.genre.Genre
import ru.vladsaybulin.model.manga.MangaKind
import ru.vladsaybulin.model.manga.Publisher

internal fun LazyListScope.mangaInformation(
    state: DetailsUiState.Success,
    onSearchByGenre: (genre: Genre) -> Unit,
    onSearchByPublisher: (publisher: Publisher) -> Unit
) {
    item(key = "manga_info") {
        MangaInformation(
            state = state,
            onPublisherClick = { onSearchByPublisher(it) },
            onGenreClick = { onSearchByGenre(it) }
        )
    }
}

@Composable
internal fun MangaInformation(
    state: DetailsUiState.Success,
    onPublisherClick: (Publisher) -> Unit,
    onGenreClick: (Genre) -> Unit,
) {
    Column {
        InfoLine(
            icon = { Icon(imageVector = SeanimeIcons.Book, contentDescription = null) },
        ) {
            MangaInfoKindAndChaptersAndVolumesText(
                kind = state.mangaKind ?: MangaKind.None,
                chapters = state.chapters,
                volumes = state.volumes
            )
        }

        InfoLine(
            icon = { Icon(imageVector = SeanimeIcons.CalendarToday, contentDescription = null) },
        ) {
            EntryInfoStatusAndDatesText(
                entryStatus = state.status,
                airedOn = state.airedOn,
                releasedOn = state.releasedOn
            )
        }

        if (!state.publishers.isNullOrEmpty()) {
            PublishersInfoLine(
                publishers = state.publishers,
                onPublisherClick = onPublisherClick
            )
        }

        if (!state.genres.isNullOrEmpty()) {
            GenresInfoLine(
                genres = state.genres,
                onGenreClick = onGenreClick
            )
        }
    }
}

@Composable
private fun PublishersInfoLine(
    publishers: ImmutableList<Publisher>,
    onPublisherClick: (Publisher) -> Unit
) {
    if (publishers.isNotEmpty()) {
        ListedInformation(
            items = publishers,
            labelSingleStringRes = R.string.publisher,
            labelSeveralStringRes = R.string.publishers,
            name = Publisher::name,
            onItemClick = { onPublisherClick(it) }
        )
    }
}