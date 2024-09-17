package ru.vladsaybulin.feature.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable
import ru.vladsaybulin.feature.profile.ProfileRoute

@Serializable
object ProfileGraphRoute

@Serializable
internal class ProfileScreenRoute(val userId: Long? = null)

fun NavController.navigateToProfileGraph(navOptions: NavOptions?) {
    navigate(ProfileGraphRoute, navOptions)
}

fun NavController.navigateToProfile(userId: Long?) {
    navigate(ProfileScreenRoute(userId))
}

fun NavGraphBuilder.profileGraph(nestedGraph: () -> Unit) {
    navigation<ProfileGraphRoute>(startDestination = ProfileScreenRoute()) {
        profileScreen()
        nestedGraph()
    }
}

fun NavGraphBuilder.profileScreen() {
    composable<ProfileScreenRoute> {
        ProfileRoute()
    }
}