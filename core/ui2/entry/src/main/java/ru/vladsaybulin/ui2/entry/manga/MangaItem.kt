package ru.vladsaybulin.ui2.entry.manga

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui2.entry.R
import ru.vladsaybulin.core.ui2.strings.compose.ProvideTitleStringsByType
import ru.vladsaybulin.core.ui2.strings.compose.asStringOrNull
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.ui2.entry.EntryGridItem
import ru.vladsaybulin.ui2.entry.EntryItemDefaults
import ru.vladsaybulin.ui2.entry.EntryItemStyle
import ru.vladsaybulin.ui2.entry.EntryListItem
import ru.vladsaybulin.ui2.entry.TitleGridItemAdditionalContent
import ru.vladsaybulin.ui2.entry.TitleListItemDefaultAdditionalContent
import ru.vladsaybulin.ui2.entry.preview.MangaItemPreviewParameterProvider

@Composable
fun MangaGridItem(
    manga: Manga,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    style: EntryItemStyle = EntryItemDefaults.regularGridStyle(),
    additionalContent: (@Composable () -> Unit)? = { MangaGridItemDefaultAdditionalContent(manga) },
) {
    EntryGridItem(
        name = manga.name,
        russianName = manga.russianName,
        poster = manga.poster,
        onClick = onClick,
        modifier = modifier,
        userRateStatus = userRateStatus,
        style = style,
        additionalContent = additionalContent
    )
}

@Composable
fun MangaListItem(
    manga: Manga,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    posterWidth: Dp = EntryItemDefaults.listItemPosterWidth,
    style: EntryItemStyle = EntryItemDefaults.regularListStyle(),
    additionalContent: (@Composable () -> Unit)? = { MangaListItemDefaultAdditionalContent(manga) },
) {
    EntryListItem(
        name = manga.name,
        russianName = manga.russianName,
        poster = manga.poster,
        onClick = onClick,
        modifier = modifier,
        userRateStatus = userRateStatus,
        posterWidth = posterWidth,
        style = style,
        additionalContent = additionalContent
    )
}

@Composable
fun MangaGridItemDefaultAdditionalContent(manga: Manga) {
    TitleGridItemAdditionalContent(
        kindStr = manga.kind.asStringOrNull(),
        year = manga.airedOn?.year
    )
}

@Composable
fun MangaListItemDefaultAdditionalContent(manga: Manga) {
    TitleListItemDefaultAdditionalContent(
        status = manga.status,
        year = manga.airedOn?.year ?: manga.releasedOn?.year,
        kindAndVolumes = chaptersAndVolumesFormatted(manga.chapters, manga.volumes),
        score = manga.score
    )
}

@Composable
@ReadOnlyComposable
private fun chaptersAndVolumesFormatted(
    chapters: Int,
    volumes: Int
): String? {
    val chaptersStr = if (chapters > 0) {
        stringResource(R.string.core_ui2_entry_manga_item_additional_content_chapters, chapters)
    } else null

    val volumesStr = if (volumes > 0) {
        pluralStringResource(R.plurals.core_ui2_entry_manga_item_additional_content_volumes, volumes, volumes)
    } else null

    return listOfNotNull(chaptersStr, volumesStr).joinToString().ifEmpty { null }
}

@Preview
@Composable
fun MangaListItemPreview(
    @PreviewParameter(MangaItemPreviewParameterProvider ::class) manga: Manga
) {
    SeanimeTheme {
        ProvideTitleStringsByType(EntryType.Manga) {
            MangaListItem(
                manga = manga,
                onClick = {}
            )
        }
    }
}

@Composable
@Preview
fun MangaGridItemPreview(
    @PreviewParameter(MangaItemPreviewParameterProvider ::class) manga: Manga
) {
    SeanimeTheme {
        ProvideTitleStringsByType(EntryType.Manga) {
            MangaGridItem(
                modifier = Modifier.width(150.dp),
                manga = manga,
                onClick = {}
            )
        }
    }
}