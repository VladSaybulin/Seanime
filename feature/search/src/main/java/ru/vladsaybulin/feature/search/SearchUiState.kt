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

package ru.vladsaybulin.feature.search

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import ru.vladsaybulin.core.ui.filters.AppliedFilters
import ru.vladsaybulin.model.search.Order
import ru.vladsaybulin.model.search.SearchType

@Immutable
data class SearchUiState(
    val selectedSearchType: SearchType,
    val selectedOrder: Order,
    val filtersLoadingState: FiltersLoadingState,
    val appliedFilters: AppliedFilters,
    val availableSearchTypes: ImmutableList<SearchType>,
    val availableOrders: ImmutableList<Order>,
    val title: SearchTitle
)