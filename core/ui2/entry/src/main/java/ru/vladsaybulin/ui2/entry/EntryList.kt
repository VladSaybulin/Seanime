package ru.vladsaybulin.ui2.entry

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui2.strings.compose.ProvideTitleStringsByType
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.ui2.entry.anime.animeItems
import ru.vladsaybulin.ui2.entry.preview.ListOfAnimesPreviewParameterProvider

@Composable
fun EntryList(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = EntryListDefaults.ContentPadding,
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = EntryListDefaults.verticalArrangement(reverseLayout),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        reverseLayout = reverseLayout,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        content = content
    )
}

object EntryListDefaults {

    val ContentPadding = PaddingValues(
        horizontal = DefaultHorizontalPadding,
        vertical = DefaultVerticalPadding
    )

    fun verticalArrangement(
        reverseLayout: Boolean = false,
        space: Dp = DefaultArrangeSpace,
    ): Arrangement.Vertical = Arrangement.spacedBy(
        space = space,
        alignment = if (reverseLayout) Alignment.Bottom else Alignment.Top
    )
}

private val DefaultVerticalPadding = 16.dp
private val DefaultHorizontalPadding = 16.dp

private val DefaultArrangeSpace = 8.dp

@Preview
@Composable
fun EntryGridPreview_Animes(@PreviewParameter(ListOfAnimesPreviewParameterProvider::class) animes: List<Anime>) {
    SeanimeTheme {
        Surface {
            ProvideTitleStringsByType(EntryType.Anime) {
                EntryList {
                    animeItems(animes, {})
                }
            }
        }
    }
}