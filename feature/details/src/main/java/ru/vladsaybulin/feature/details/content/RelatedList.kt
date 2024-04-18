package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.vladsaybulin.core.ui.Header
import ru.vladsaybulin.feature.details.R
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.related.RelatedEntry
import kotlin.math.min

fun LazyListScope.relatedItems(
    relatedEntries: ImmutableList<RelatedEntry>,
    onEntryClick: (EntryType, Long) -> Unit,
    onShowAllClick: () -> Unit,
    itemModifier: Modifier = Modifier,
    headerKey: String = "related_header",
    itemPrefixKey: String = "related_"
) {
    val shownRelatedSize = min(relatedEntries.size, MaxShownRelatedEntriesSize)
    val showShowAll = shownRelatedSize < relatedEntries.size

    item(key = headerKey) {
        Header(
            modifier = Modifier.clickable(
                onClick = onShowAllClick,
                enabled = showShowAll
            ),
        ) {
            ShowAllHeaderText(
                headerText = stringResource(id = R.string.related),
                shouldShownShowAll = showShowAll
            )
        }
    }

    items(
        count = shownRelatedSize,
        key = {
            relatedEntries[it].run {
                val itemKey = if (anime != null) "a_${anime!!.id}" else "m_${manga!!.id}"
                "$itemPrefixKey$itemKey"
            }
        }
    ) {
        val relatedEntry = relatedEntries[it]
        RelatedEntryListItem(
            relatedEntry = relatedEntry,
            onEntryClick = onEntryClick,
            modifier = itemModifier.padding(horizontal = 8.dp)
        )
    }
}

private const val MaxShownRelatedEntriesSize = 3
