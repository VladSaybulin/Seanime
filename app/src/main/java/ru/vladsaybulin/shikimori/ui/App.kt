package ru.vladsaybulin.shikimori.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.feature.calendar.navigation.calendarScreen
import ru.vladsaybulin.feature.details.navigation.detailsScreen
import ru.vladsaybulin.feature.details.navigation.navigateToDetails
import ru.vladsaybulin.model.EntryType

@Composable
fun App() {
    val navController = rememberNavController()

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
                    onBackClick = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}