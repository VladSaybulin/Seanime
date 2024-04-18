package ru.vladsaybulin.model.common

data class StatisticsItem<T>(
    val values: T,
    val count: Int
)