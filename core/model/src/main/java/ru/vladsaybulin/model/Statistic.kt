package ru.vladsaybulin.model

data class Statistic<T>(
    val values: T,
    val count: Int
)