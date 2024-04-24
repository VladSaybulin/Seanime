package ru.vladsaybulin.shikimori

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import dagger.hilt.android.AndroidEntryPoint
import ru.vladsaybulin.core.auth.ShikimoriLoginAction
import ru.vladsaybulin.shikimori.ui.ShikimoriApp
import ru.vladsaybulin.shikimori.ui.rememberShikimoriAppState
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var loginAction: ShikimoriLoginAction

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginAction.register(this)

        enableEdgeToEdge()

        setContent {
            ShikimoriApp(
                appState = rememberShikimoriAppState(
                    windowSizeClass = calculateWindowSizeClass(this)
                ),
                signIn = { loginAction() }
            )
        }
    }
}
