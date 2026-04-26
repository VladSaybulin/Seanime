package ru.vladsaybulin.core.ui2.entry

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.core.ui2.entry.manga.mangaItems
import ru.vladsaybulin.core.ui2.entry.preview.ListOfMangaPreviewParameterProvider

@Composable
fun EntryGrid(
    modifier: Modifier = Modifier,
    columns: GridCells = EntryGridDefaults.Columns,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = EntryGridDefaults.ContentPadding,
    reverseLayout: Boolean = false,
    horizontalArrangement: Arrangement.Horizontal = EntryGridDefaults.horizontalArrangement(reverseLayout),
    verticalArrangement: Arrangement.Vertical = EntryGridDefaults.verticalArrangement(reverseLayout),
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyGridScope.() -> Unit
) {
    LazyVerticalGrid(
        columns = columns,
        state = state,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        modifier = modifier,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        reverseLayout = reverseLayout,
        content = content
    )
}

object EntryGridDefaults {

    val ContentPadding = PaddingValues(
        horizontal = DefaultHorizontalPadding,
        vertical = DefaultVerticalPadding
    )

    val Columns = GridCells.Adaptive(DefaultMinWidth)

    fun verticalArrangement(reverseLayout: Boolean, space: Dp = DefaultVerticalArrangeSpace) = Arrangement.spacedBy(
        space = space,
        alignment = if (reverseLayout) Alignment.Bottom else Alignment.Top
    )

    fun horizontalArrangement(reverseLayout: Boolean, space: Dp = DefaultHorizontalArrangeSpace) = Arrangement.spacedBy(
        space = space,
        alignment = if (reverseLayout) Alignment.End else Alignment.Start
    )
}

private val DefaultMinWidth = 150.dp
private val DefaultVerticalPadding = 16.dp
private val DefaultHorizontalPadding = 16.dp

private val DefaultVerticalArrangeSpace = 8.dp
private val DefaultHorizontalArrangeSpace = 8.dp

@Preview
@Composable
fun EntryGridPreview_Mangas(@PreviewParameter(ListOfMangaPreviewParameterProvider ::class) mangas: List<Manga>) {
    SeanimeTheme {
        Surface {
            EntryGrid {
                mangaItems(mangas, {})
            }
        }
    }
}