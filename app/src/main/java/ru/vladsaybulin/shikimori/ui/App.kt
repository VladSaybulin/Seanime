package ru.vladsaybulin.shikimori.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.feature.calendar.navigation.calendarScreen
import ru.vladsaybulin.feature.details.navigation.detailsScreen
import ru.vladsaybulin.feature.details.navigation.navigateToDetails
import ru.vladsaybulin.feature.imageview.Image
import ru.vladsaybulin.feature.imageview.ImageViewViewModel
import ru.vladsaybulin.feature.imageview.navigation.imageViewScreen
import ru.vladsaybulin.feature.imageview.navigation.navigateToImageView
import ru.vladsaybulin.model.EntryType

@Composable
fun App() {
    val navController = rememberNavController()
    val imageViewViewModel = hiltViewModel<ImageViewViewModel>()

    ShikimoriTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = "calendar"
            ) {
                calendarScreen(
                    openAnimeDetails = { navController.navigateToDetails(EntryType.Anime, it) }
                )
                detailsScreen(
                    openEntryDetails = { type, id -> navController.navigateToDetails(type, id) },
                    openScreenshot = { screenshots, initialIndex ->
                        imageViewViewModel.setup(
                            screenshots.map { Image(it.originalUrl) },
                            initialIndex
                        )
                        navController.navigateToImageView()
                    },
                    onBackClick = {
                        navController.navigateUp()
                    }
                )
                imageViewScreen(
                    viewModel = imageViewViewModel,
                    onBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}