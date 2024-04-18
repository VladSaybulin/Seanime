package ru.vladsaybulin.data.model

import ru.vladsaybulin.model.common.StatisticsItem
import ru.vladsaybulin.network.models.common.NetworkStatisticsItem

fun <T> NetworkStatisticsItem<T>.asExternalModel() = StatisticsItem(values, count)