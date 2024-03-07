package ru.vladsaybulin.data.model

import ru.vladsaybulin.core.network.graphql.type.UserRateStatusEnum
import ru.vladsaybulin.model.UserRateStatus

fun UserRateStatusEnum?.asUserRateStatus() = when (this) {
    UserRateStatusEnum.planned -> UserRateStatus.Planned
    UserRateStatusEnum.watching -> UserRateStatus.Watching
    UserRateStatusEnum.rewatching -> UserRateStatus.Rewatching
    UserRateStatusEnum.completed -> UserRateStatus.Completed
    UserRateStatusEnum.on_hold -> UserRateStatus.OnHold
    UserRateStatusEnum.dropped -> UserRateStatus.Dropped
    else -> UserRateStatus.None
}