package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.RelatedEntry

@Composable
fun RelatedBottomSheetContent(
    related: List<RelatedEntry>,
    onEntryClick: (EntryType, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(items = related) {
            RelatedEntryListItem(
                relatedEntry = it,
                onEntryClick = onEntryClick
            )
        }
    }
}