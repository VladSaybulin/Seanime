package ru.vladsaybulin.network.mapper.enums

import ru.vladsaybulin.core.network.graphql.type.UserRateStatusEnum
import ru.vladsaybulin.model.userrate.UserRateStatus

internal fun UserRateStatusEnum?.asUserRateStatus() = when (this) {
    UserRateStatusEnum.planned -> UserRateStatus.Planned
    UserRateStatusEnum.watching -> UserRateStatus.Watching
    UserRateStatusEnum.rewatching -> UserRateStatus.Rewatching
    UserRateStatusEnum.completed -> UserRateStatus.Completed
    UserRateStatusEnum.on_hold -> UserRateStatus.OnHold
    UserRateStatusEnum.dropped -> UserRateStatus.Dropped
    else -> UserRateStatus.None
}

internal fun UserRateStatus.asUserRateStatusEnum() = when (this) {
    UserRateStatus.Planned -> UserRateStatusEnum.planned
    UserRateStatus.Watching -> UserRateStatusEnum.watching
    UserRateStatus.Rewatching -> UserRateStatusEnum.rewatching
    UserRateStatus.Completed -> UserRateStatusEnum.completed
    UserRateStatus.OnHold -> UserRateStatusEnum.on_hold
    UserRateStatus.Dropped -> UserRateStatusEnum.dropped
    else -> UserRateStatusEnum.UNKNOWN__
}