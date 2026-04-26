package ru.vladsaybulin.core.ui.entry.metadata

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.core.ui2.strings.MangaStrings
import ru.vladsaybulin.core.ui2.strings.compose.asStringOrNull
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.manga.MangaKind

@Composable
fun DefaultMangaGridItemMetadata(manga: Manga) {
    MangaMetadataComponents.KindAndYearLine(manga)
}

@Composable
fun DefaultMangaListItemMetadata(manga: Manga) {
    Column {
        MangaMetadataComponents.KindChaptersVolumes(
            kind = manga.kind,
            chapters = manga.chapters,
            volumes = manga.volumes
        )

        TitleMetadataComponents.TitleStatusAndDatesRow(
            entryStatus = manga.status,
            airedOn = manga.airedOn,
            releasedOn = manga.releasedOn
        )

        TitleMetadataComponents.Score(score = manga.score)
    }
}

object MangaMetadataComponents {

    @Composable
    fun KindAndYearLine(manga: Manga) {
        TitleMetadataComponents.KindAndYearLine(
            kindStringId = MangaStrings.kindId(manga.kind),
            year = manga.airedOn?.year ?: manga.releasedOn?.year
        )
    }

    @Composable
    fun KindChaptersVolumes(kind: MangaKind, chapters: Int, volumes: Int) {
        val chaptersStr = if (chapters > 0) stringResource(id = R.string.core_ui_manga_metadata_chapters, chapters) else null
        val volumesStr = if (volumes > 0) stringResource(id = R.string.core_ui_manga_metadata_volumes, volumes) else null
        val chaptersVolumesStr = when {
            chaptersStr != null && volumesStr != null -> "$chaptersStr, $volumesStr"
            chaptersStr !=  null -> chaptersStr
            volumesStr != null -> volumesStr
            else -> null
        }

        val kindStr = kind.asStringOrNull()

        if (kindStr != null && chaptersVolumesStr != null) {
            Text(text = "$kindStr, $chaptersVolumesStr")
        } else if (kindStr != null) {
            Text(text = kindStr)
        } else if (chaptersVolumesStr != null) {
            Text(text = chaptersVolumesStr)
        }

    }
}