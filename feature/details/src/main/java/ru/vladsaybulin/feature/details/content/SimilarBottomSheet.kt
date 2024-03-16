package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.entry.EntryGridItem
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.SimilarEntry

@Composable
fun SimilarBottomSheetContent(
    similarEntries: List<SimilarEntry>,
    onEntryClick: (EntryType, Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(items = similarEntries) { entry ->
            EntryGridItem(
                name = entry.run { russianName ?: originalName },
                poster = entry.poster,
                onClick = { onEntryClick(entry.entryType, entry.entryId) },
                modifier = modifier.fillMaxWidth(),
                nameTextStyle = ShikimoriTheme.typography.labelLarge,
                shape = ShikimoriTheme.shapes.large
            )
        }
    }
}

