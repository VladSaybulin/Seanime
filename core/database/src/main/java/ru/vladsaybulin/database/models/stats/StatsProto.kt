package ru.vladsaybulin.database.models.stats

import kotlinx.serialization.Serializable
import ru.vladsaybulin.model.common.StatisticsItem

@Serializable
class StatsProto<V>(val items: List<StatsItemProto<V>>)

fun <V> StatsProto<V>.asExternalModel(): List<StatisticsItem<V>> =
    items.map { StatisticsItem(it.value, it.count) }