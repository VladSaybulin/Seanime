package ru.vladsaybulin.ui2.entry

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.ui2.entry.anime.animeCarouselItems
import ru.vladsaybulin.ui2.entry.preview.ListOfAnimesPreviewParameterProvider

@Composable
fun EntryCarousel(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = EntryCarouselDefaults.contentPadding,
    reverseLayout: Boolean = false,
    horizontalArrangement: Arrangement.Horizontal = EntryCarouselDefaults.horizontalArrangement(reverseLayout),
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit
) {
    LazyRow(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        content = content
    )
}

object EntryCarouselDefaults {
    val contentPadding = PaddingValues(
        horizontal = DefaultHorizontalPadding,
        vertical = DefaultVerticalPadding
    )

    fun horizontalArrangement(reverseLayout: Boolean = false) =
        Arrangement.spacedBy(
            space = DefaultArrangeSpace,
            alignment = if (!reverseLayout) Alignment.Start else Alignment.End
        )
}

private val DefaultVerticalPadding = 16.dp
private val DefaultHorizontalPadding = 8.dp

private val DefaultArrangeSpace = 4.dp

@Preview
@Composable
fun EntryCarouselPreview(@PreviewParameter(ListOfAnimesPreviewParameterProvider ::class) animes: List<Anime>) {
    SeanimeTheme {
        Surface {
            EntryCarousel {
                animeCarouselItems(animes, {}, itemModifier = Modifier.width(128.dp))
            }
        }
    }
}