package ru.vladsaybulin.shikimori

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.android.AndroidEntryPoint
import ru.vladsaybulin.feature.calendar.CalendarRoute
import ru.vladsaybulin.shikimori.ui.theme.ShikimoriTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShikimoriTheme {
                CalendarRoute()
            }
        }
    }
}
