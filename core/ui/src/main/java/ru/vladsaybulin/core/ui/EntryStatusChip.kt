package ru.vladsaybulin.core.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.ui.colors.entryStatusColor
import ru.vladsaybulin.core.ui.strings.animeStatusStringResource
import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.model.EntryType

@Composable
fun EntryStatusChip(entryStatus: EntryStatus, entryType: EntryType = EntryType.Anime) {

    val color = entryStatusColor(entryStatus = entryStatus)

    Box(
        modifier = Modifier
            .padding(vertical = 2.dp)
            .border(
                width = BorderStrokeWidth,
                color = color,
                shape = MaterialTheme.shapes.extraSmall
            )
            .padding(ChipContentPadding)
    ) {
        Text(
            text = checkNotNull(
                when (entryType) {
                    EntryType.Anime -> animeStatusStringResource(status = entryStatus)
                    else -> "MangaKind"
                }
            ),
            color = color
        )
    }
}

private val BorderStrokeWidth = 1.dp
private val ChipContentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)