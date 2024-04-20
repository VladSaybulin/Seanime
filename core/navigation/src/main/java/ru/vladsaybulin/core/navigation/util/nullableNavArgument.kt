package ru.vladsaybulin.core.navigation.util

import androidx.navigation.NavArgumentBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument

fun nullableNavArgument(
    name: String,
    builder: NavArgumentBuilder.() -> Unit = {}
) = navArgument(name) {
    type = NavType.StringType
    nullable = true
    defaultValue = null
    builder()
}