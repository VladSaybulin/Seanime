package ru.vladsaybulin.core.ui.strings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.model.EntryType

fun entryTypeStringId(entryType: EntryType) = when (entryType) {
    EntryType.Anime -> R.string.core_ui_entry_type_anime
    EntryType.Manga -> R.string.core_ui_entry_type_manga
}

@Composable
fun entryTypeString(entryType: EntryType) = stringResource(id = entryTypeStringId(entryType))