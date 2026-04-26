/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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