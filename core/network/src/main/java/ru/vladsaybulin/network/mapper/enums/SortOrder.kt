package ru.vladsaybulin.network.mapper.enums

import ru.vladsaybulin.core.network.graphql.type.SortOrderEnum
import ru.vladsaybulin.model.list.UserRateOrder
import ru.vladsaybulin.model.list.UserRateOrder.Asc
import ru.vladsaybulin.model.list.UserRateOrder.Desc

fun UserRateOrder.asSortOrderEnum() = when (this) {
    Asc -> SortOrderEnum.asc
    Desc -> SortOrderEnum.desc
}