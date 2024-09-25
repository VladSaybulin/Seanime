package ru.vladsaybulin.network.mapper.data

import ru.vladsaybulin.core.network.graphql.type.UserRateOrderInputType
import ru.vladsaybulin.model.list.UserRateOrder
import ru.vladsaybulin.model.list.UserRateOrderField
import ru.vladsaybulin.network.mapper.enums.asSortOrderEnum
import ru.vladsaybulin.network.mapper.enums.asUserRateOrderFieldEnum

fun Pair<UserRateOrderField, UserRateOrder>.asUserRateOrderInputType() = UserRateOrderInputType(
    field = first.asUserRateOrderFieldEnum(),
    order = second.asSortOrderEnum()
)
