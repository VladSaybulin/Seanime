package ru.vladsaybulin.core.navigation.util

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

fun NavGraphBuilder.withParentGraphRoute(screenRoute: String): String {
    val parentGraphRoute = route
    return if (parentGraphRoute != null) "$parentGraphRoute/$screenRoute" else screenRoute
}

fun NavController.withParentGraphRoute(screenRoute: String): String {
    val parentGraphRoute = currentBackStackEntry?.destination?.parent?.route
    return if (parentGraphRoute != null) "$parentGraphRoute/$screenRoute" else screenRoute
}