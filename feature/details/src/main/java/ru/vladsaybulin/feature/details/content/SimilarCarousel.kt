package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.components.ShikimoriCarousel
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.ContentWithClickableHeader
import ru.vladsaybulin.core.ui.entry.EntryGridItem
import ru.vladsaybulin.feature.details.R
import ru.vladsaybulin.model.SimilarEntry
import ru.vladsaybulin.model.common.EntryType
import kotlin.math.min

@Composable
fun SimilarCarousel(
    similarEntries: List<SimilarEntry>,
    onEntryClick: (EntryType, Long) -> Unit,
    onShowAll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shownSimilarEntriesSize = min(similarEntries.size, MaxShownSimilarEntriesSize)
    val showShowAll = shownSimilarEntriesSize < similarEntries.size

    ContentWithClickableHeader(
        headerText = {
            ShowAllHeaderText(
                headerText = stringResource(id = R.string.similar),
                shouldShownShowAll = showShowAll
            )
        },
        onClick = onShowAll,
        enabled = showShowAll,
        modifier = modifier
    ) {
        ShikimoriCarousel {
            items(
                count = shownSimilarEntriesSize,
                key = {
                    //Similar entries have the same EntryType, so the entryId is sufficient for the key
                    similarEntries[it].entryId
                }
            ) {
                val entry = similarEntries[it]
                SimilarEntryCard(
                    entry = entry,
                    onClick = { onEntryClick(entry.entryType, entry.entryId) },
                )
            }
        }
    }
}

@Composable
private fun SimilarEntryCard(
    entry: SimilarEntry,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    EntryGridItem(
        name = entry.run { russianName ?: originalName },
        imageUrl = entry.poster?.previewUrl,
        onClick = onClick,
        modifier = modifier.width(SimilarCardWidth),
        nameTextStyle = ShikimoriTheme.typography.labelSmall
    )


}

private const val MaxShownSimilarEntriesSize = 10
private val SimilarCardWidth = 128.dp