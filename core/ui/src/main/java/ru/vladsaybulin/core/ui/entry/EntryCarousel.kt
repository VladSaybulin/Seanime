package ru.vladsaybulin.core.ui.entry

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vladsaybulin.model.Poster
import ru.vladsaybulin.model.UserRateStatus


fun <T> LazyListScope.entryCarouselItems(
    items: List<T>,
    name: (T) -> String,
    poster: (T) -> Poster?,
    key: (T) -> Any,
    onClick: (T) -> Unit,
    itemModifier: Modifier = Modifier,
    userRateStatus: ((T) -> UserRateStatus)? = null,
    details: (@Composable (T) -> Unit)? = null
) {

}