package ru.vladsaybulin.model.search

data class FilterOption<T> (
    val value: T,
    val serializedValue: String
)