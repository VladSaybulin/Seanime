package ru.vladsaybulin.model.common

data class DataSlice<T>(
    val data: List<T>,
    val hasMore: Boolean
)