package ru.vladsaybulin.feature.imageview.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.vladsaybulin.feature.imageview.ImageViewRoute
import ru.vladsaybulin.feature.imageview.ImageViewViewModel

fun NavController.navigateToImageView() {
    navigate("imageview")
}

fun NavGraphBuilder.imageViewScreen(
    viewModel: ImageViewViewModel,
    onBack: () -> Unit
) {
    composable(route = "imageview") {
        ImageViewRoute(
            viewModel = viewModel,
            onBack = onBack
        )
    }
}