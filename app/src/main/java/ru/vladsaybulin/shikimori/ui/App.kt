package ru.vladsaybulin.shikimori.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import ru.vladsaybulin.feature.userrate.UserRateBottomSheet
import ru.vladsaybulin.feature.userrate.UserRateViewModel
import ru.vladsaybulin.model.EntryType

@Composable
fun App() {
    val navController = rememberNavController()
    val imageViewViewModel = hiltViewModel<ImageViewViewModel>()
    val userRateViewViewModel = hiltViewModel<UserRateViewModel>()

    ShikimoriTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            var showUserRateBottomSheet by remember { mutableStateOf(false) }

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
                    openUserRate = { setup ->
                        userRateViewViewModel.setupUserRate(setup)
                        showUserRateBottomSheet = true
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

            if (showUserRateBottomSheet) {
                UserRateBottomSheet(
                    viewModel = userRateViewViewModel,
                    onDismissRequest = { showUserRateBottomSheet = false }
                )
            }
        }
    }
}