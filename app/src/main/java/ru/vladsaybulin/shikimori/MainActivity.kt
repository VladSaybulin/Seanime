package ru.vladsaybulin.shikimori

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import dagger.hilt.android.AndroidEntryPoint
import ru.vladsaybulin.core.auth.AuthorizationLauncher
import ru.vladsaybulin.core.auth.ShikimoriAuthState
import ru.vladsaybulin.core.auth.launch
import ru.vladsaybulin.core.auth.registerAuthorizationLauncher
import ru.vladsaybulin.shikimori.ui.ShikimoriApp
import ru.vladsaybulin.shikimori.ui.rememberShikimoriAppState
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var authState: ShikimoriAuthState

    private lateinit var authorizationLauncher: AuthorizationLauncher

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authorizationLauncher = registerAuthorizationLauncher(authState)

        enableEdgeToEdge()

        setContent {
            ShikimoriApp(
                appState = rememberShikimoriAppState(
                    windowSizeClass = calculateWindowSizeClass(this)
                ),
                signIn = { authorizationLauncher.launch() }
            )
        }
    }
}
