package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.stats.StatsItemProto
import ru.vladsaybulin.database.models.stats.StatsProto
import ru.vladsaybulin.network.models.common.NetworkStatisticsItem

fun <V> List<NetworkStatisticsItem<V>>?.asDbModel() =
    StatsProto(this?.map { StatsItemProto(it.values, it.count) } ?: emptyList())