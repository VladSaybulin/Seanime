package ru.vladsaybulin.feature.search

import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.Order

data class SearchControlPanelUiState(
    val isEntryTypeLocked: Boolean,
    val currentEntryType: EntryType,
    val currentOrder: Order,
    val availableEntryTypes: List<EntryType> = EntryType.entries,
    val availableOrders: List<Order> = Order.entries,
)