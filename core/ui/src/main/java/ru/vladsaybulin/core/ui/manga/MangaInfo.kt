package ru.vladsaybulin.core.ui.manga

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.core.ui.entry.EntryInfoKindAndYear
import ru.vladsaybulin.core.ui.strings.mangaKindString
import ru.vladsaybulin.model.manga.MangaKind

@Composable
fun MangaInfoKindAndYearText(
    kind: MangaKind,
    year: Int?
) {
    EntryInfoKindAndYear(
        kindText = if (kind != MangaKind.None) mangaKindString(kind) else null,
        year = year
    )
}

@Composable
fun MangaInfoKindAndChaptersAndVolumesText(
    kind: MangaKind,
    chapters: Int,
    volumes: Int
) {
    val kindText = if (kind != MangaKind.None) mangaKindString(kind) else null

    val chaptersText = if (chapters > 0) {
        pluralStringResource(
            id = R.plurals.core_ui_manga_info_chapters,
            count = chapters,
            chapters
        )
    } else null

    val volumesText = if (volumes > 0 ) {
        pluralStringResource(
            id = R.plurals.core_ui_manga_info_chapters,
            count = chapters,
            chapters
        )
    } else null

    if (kindText != null || chaptersText != null || volumesText != null) {
        val separator = stringResource(id = R.string.core_ui_info_separator)
        Text(
            text = listOfNotNull(
                kindText,
                chaptersText,
                volumesText
            ).joinToString(separator = separator)
        )
    }
}