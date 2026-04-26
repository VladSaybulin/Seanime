package ru.vladsaybulin.core.ui2.entry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal fun EntryItemLayout(
    poster: @Composable (Modifier) -> Unit,
    info: @Composable (Modifier) -> Unit,
    horizontal: Boolean
) {
    if (horizontal) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            poster(Modifier.fillMaxHeight())
            info(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }
    } else {
        Column {
            poster(Modifier)
            info(Modifier.fillMaxWidth())
        }
    }
}
