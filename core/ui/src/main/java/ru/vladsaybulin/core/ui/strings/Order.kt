package ru.vladsaybulin.core.ui.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.model.search.Order

fun orderStringId(order: Order) = when (order) {
    Order.Popularity -> R.string.core_ui_order_popularity
    Order.Ranked -> R.string.core_ui_order_ranked
    Order.Alphabet -> R.string.core_ui_order_alphabet
    Order.Created -> R.string.core_ui_order_created
    Order.CreatedDesc -> R.string.core_ui_order_created_desc
    Order.Random -> R.string.core_ui_order_random
}

@Composable
@ReadOnlyComposable
fun orderString(order: Order) = stringResource(id = orderStringId(order))