package ru.vladsaybulin.core.ui.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType

fun entryStatusStringId(status: EntryStatus, titleType: EntryType) = when (titleType) {
    EntryType.Anime -> animeStatusStringId(status)
    EntryType.Manga -> mangaStatusStringId(status)
}

@Composable
@ReadOnlyComposable
fun entryStatusStringId(status: EntryStatus) =
    entryStatusStringId(status, LocalTitleStrings.current)

@Composable
@ReadOnlyComposable
fun entryStatusString(status: EntryStatus, titleType: EntryType) =
    entryStatusStringId(status, titleType)?.let { stringResource(id = it) }

@Composable
@ReadOnlyComposable
fun entryStatusString(status: EntryStatus) =
    entryStatusStringId(status)?.let { stringResource(id = it) } ?: status.name

private fun animeStatusStringId(status: EntryStatus) = when (status) {
    EntryStatus.Anons -> R.string.anime_status_anons
    EntryStatus.Ongoing -> R.string.anime_status_ongoing
    EntryStatus.Released -> R.string.anime_status_released
    else -> null
}

private fun mangaStatusStringId(status: EntryStatus) = when (status) {
    EntryStatus.Anons -> R.string.manga_status_anons
    EntryStatus.Ongoing -> R.string.manga_status_ongoing
    EntryStatus.Released -> R.string.manga_status_released
    EntryStatus.Paused -> R.string.manga_status_paused
    EntryStatus.Discontinued -> R.string.manga_status_discontinued
    EntryStatus.None -> null
}



