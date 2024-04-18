package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.ImmutableList
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.navigation.SearchArgs
import ru.vladsaybulin.core.ui.strings.chaptersAndVolumesString
import ru.vladsaybulin.core.ui.strings.mangaKindString
import ru.vladsaybulin.feature.details.DetailsUiState
import ru.vladsaybulin.feature.details.R
import ru.vladsaybulin.feature.details.genreSearchParams
import ru.vladsaybulin.feature.details.publisherSearchParams
import ru.vladsaybulin.model.Publisher

internal fun LazyListScope.mangaInformation(
    state: DetailsUiState.Success,
    onSearchClick: (SearchArgs) -> Unit
) {
    item(key = "manga_info") {
        MangaInformation(
            state = state,
            onPublisherClick = { onSearchClick(state.publisherSearchParams(it)) },
            onGenreClick = { onSearchClick(state.genreSearchParams(it)) }
        )
    }
}

@Composable
fun MangaInformation(
    state: DetailsUiState.Success,
    onPublisherClick: (Long) -> Unit,
    onGenreClick: (Long) -> Unit,
) {
    Column {
        KindAndVolumeInfoLine(state = state)

        StatusAndDatesInfoLine(
            airedOn = state.airedOn,
            releasedOn = state.releasedOn,
            status = state.status
        )

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
private fun KindAndVolumeInfoLine(state: DetailsUiState.Success) {
    val kind = mangaKindString(mangaKind = checkNotNull(state.mangaKind))
    val chaptersAndVolumes = chaptersAndVolumesString(
        volumes = state.volumes,
        chapters = state.chapters
    )

    if (kind != null || chaptersAndVolumes != null) {
        InfoLine(
            icon = { InfoIcon(imageVector = ShikimoriIcons.Book) }
        ) {
            Text(
                text = listOfNotNull(kind, chaptersAndVolumes).joinToString(separator = ",")
            )
        }
    }
}

@Composable
private fun PublishersInfoLine(
    publishers: ImmutableList<Publisher>,
    onPublisherClick: (Long) -> Unit
) {
    if (publishers.isNotEmpty()) {
        ListedInformation(
            items = publishers,
            labelSingleStringRes = R.string.publisher,
            labelSeveralStringRes = R.string.publishers,
            name = Publisher::name,
            annotation = { it.id.toString() },
            onItemClick = { onPublisherClick(it.toLong()) }
        )
    }
}