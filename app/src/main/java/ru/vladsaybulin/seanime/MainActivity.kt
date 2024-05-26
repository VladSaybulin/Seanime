package ru.vladsaybulin.seanime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.compose.LocalLifecycleOwner
import dagger.hilt.android.AndroidEntryPoint
import ru.vladsaybulin.core.auth.ShikimoriLoginAction
import ru.vladsaybulin.seanime.ui.SeanimeApp
import ru.vladsaybulin.seanime.ui.rememberSeanimeAppState
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
            CompositionLocalProvider(value = LocalLifecycleOwner provides this) { //TODO FIX. Not provided
                SeanimeApp(
                    appState = rememberSeanimeAppState(
                        windowSizeClass = calculateWindowSizeClass(this),
                        onAuth = loginAction::invoke,
                        onExternalLink = {}
                    )
                )
            }
        }
    }
}
