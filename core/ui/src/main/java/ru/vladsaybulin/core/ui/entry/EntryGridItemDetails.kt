package ru.vladsaybulin.core.ui.entry

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class EntryGridItemDetailsData(
    val kindText: String?,
    val year: Int?
)

@Composable
internal fun EntryGridItemDetails(
    modifier: Modifier = Modifier,
    data: EntryGridItemDetailsData
) {
    Box(modifier = modifier) {
        KindAndYearText(kind = data.kindText, year = data.year?.toString())
    }
}
