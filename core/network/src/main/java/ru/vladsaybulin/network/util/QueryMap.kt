package ru.vladsaybulin.network.util

import ru.vladsaybulin.core.network.graphql.type.OrderEnum
import ru.vladsaybulin.model.search.QueryMapKey

fun Map<QueryMapKey, String>.getOrderEnum() =
    get(QueryMapKey.Order)?.let { OrderEnum.safeValueOf(it) }