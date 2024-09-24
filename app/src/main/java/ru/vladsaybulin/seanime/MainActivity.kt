package ru.vladsaybulin.seanime

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.compose.LocalLifecycleOwner
import dagger.hilt.android.AndroidEntryPoint
import ru.vladsaybulin.core.auth.ShikimoriAuthorization
import ru.vladsaybulin.seanime.ui.SeanimeApp
import ru.vladsaybulin.seanime.ui.rememberSeanimeAppState
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var shikimoriAuthorization: ShikimoriAuthorization

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shikimoriAuthorization.registerLoginAction(this)

        enableEdgeToEdge()

        setContent {
            SeanimeApp(
                appState = rememberSeanimeAppState(windowSizeClass = calculateWindowSizeClass(this)),
                onAuth = shikimoriAuthorization::login
            )
        }
    }
}
