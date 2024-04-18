package ru.vladsaybulin.feature.imageview.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.vladsaybulin.feature.imageview.ImageViewRoute
import ru.vladsaybulin.feature.imageview.ImageViewViewModel

const val IMAGE_VIEW_ROUTE = "imageview"

fun NavController.navigateToImageView() {
    navigate(IMAGE_VIEW_ROUTE)
}

fun NavGraphBuilder.imageViewScreen(
    viewModel: ImageViewViewModel,
    onBackClick: () -> Unit,
) {
    composable(route = IMAGE_VIEW_ROUTE) {
        ImageViewRoute(
            viewModel = viewModel,
            onBackClick = onBackClick
        )
    }
}