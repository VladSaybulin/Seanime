package ru.vladsaybulin.network.mapper.enums

import ru.vladsaybulin.core.network.graphql.type.UserRateOrderFieldEnum
import ru.vladsaybulin.model.list.UserRateOrderField

fun UserRateOrderField.asUserRateOrderFieldEnum() = when (this) {
    UserRateOrderField.CreatedAt -> UserRateOrderFieldEnum.id
    UserRateOrderField.UpdatedAt -> UserRateOrderFieldEnum.updated_at
}