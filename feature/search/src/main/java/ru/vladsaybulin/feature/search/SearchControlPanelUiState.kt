package ru.vladsaybulin.feature.search

import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.search.Order

data class SearchControlPanelUiState(
    val isEntryTypeLocked: Boolean,
    val currentEntryType: EntryType,
    val currentOrder: Order,
    val availableEntryTypes: List<EntryType> = EntryType.entries,
    val availableOrders: List<Order> = Order.entries,
)