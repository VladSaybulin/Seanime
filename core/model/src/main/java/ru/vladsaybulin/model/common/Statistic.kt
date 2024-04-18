package ru.vladsaybulin.model.common

data class Statistic<T>(
    val values: T,
    val count: Int
)