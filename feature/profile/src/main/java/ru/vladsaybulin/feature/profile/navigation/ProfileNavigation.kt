package ru.vladsaybulin.feature.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import ru.vladsaybulin.feature.profile.ProfileRoute

@Serializable
internal class ProfileScreenRoute(val userId: Long? = null)

fun NavController.navigateToMe() {
    navigate(ProfileScreenRoute(null))
}

fun NavController.navigateToUser(userId: Long) {
    navigate(ProfileScreenRoute(userId))
}

fun NavGraphBuilder.profileScreen() {
    composable<ProfileScreenRoute> {
        ProfileRoute()
    }
}