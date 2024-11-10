package ru.vladsaybulin.core.ui.entry.list

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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

    val ContentPadding = PaddingValues(horizontal = 2.dp, vertical = 16.dp)

    fun verticalArrangement(
        reverseLayout: Boolean = false,
        space: Dp = DefaultArrangeSpace,
    ): Arrangement.Vertical = Arrangement.spacedBy(
        space = space,
        alignment = if (reverseLayout) Alignment.Bottom else Alignment.Top
    )
}

private val DefaultArrangeSpace = 4.dp