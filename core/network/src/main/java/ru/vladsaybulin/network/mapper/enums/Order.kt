package ru.vladsaybulin.network.mapper.enums

import ru.vladsaybulin.core.network.graphql.type.OrderEnum
import ru.vladsaybulin.model.Order

internal fun Order.asOrderEnum() = when (this) {
    Order.Popularity -> OrderEnum.popularity
    Order.Ranked -> OrderEnum.ranked
    Order.Alphabet -> OrderEnum.name_
    Order.Created -> OrderEnum.created_at
    Order.CreatedDesc -> OrderEnum.created_at_desc
    Order.Random -> OrderEnum.random
}