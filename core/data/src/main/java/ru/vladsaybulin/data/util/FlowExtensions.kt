package ru.vladsaybulin.data.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T> flowOf(block: suspend () -> T): Flow<T> = flow {
    emit(block())
}