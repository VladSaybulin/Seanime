package ru.vladsaybulin.core.navigation.util

fun StringBuilder.appendArg(key: String, value: String) {
    if (isNotEmpty()) {
        append("&")
    }
    append(key).append("=").append(value)
}