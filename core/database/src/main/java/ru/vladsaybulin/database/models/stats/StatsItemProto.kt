package ru.vladsaybulin.database.models.stats

import kotlinx.serialization.Serializable

@Serializable
class StatsItemProto<V>(
    val value: V,
    val count: Int
)