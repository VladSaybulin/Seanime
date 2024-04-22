package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.components.ShikimoriModalBottomSheet
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.entry.EntryGridItem
import ru.vladsaybulin.model.SimilarEntry
import ru.vladsaybulin.model.common.EntryType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SimilarBottomSheet(
    similarEntries: List<SimilarEntry>,
    onEntryClick: (EntryType, Long) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ShikimoriModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        SimilarBottomSheetContent(
            similarEntries = similarEntries,
            onEntryClick = onEntryClick
        )
    }
}

@Composable
private fun SimilarBottomSheetContent(
    similarEntries: List<SimilarEntry>,
    onEntryClick: (EntryType, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        items(items = similarEntries) { entry ->
            EntryGridItem(
                name = entry.run { russianName ?: originalName },
                imageUrl = entry.poster?.previewUrl,
                onClick = { onEntryClick(entry.entryType, entry.entryId) },
                modifier = Modifier.fillMaxWidth(),
                nameTextStyle = ShikimoriTheme.typography.labelLarge,
                shape = ShikimoriTheme.shapes.large
            )
        }
    }
}

