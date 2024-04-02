package ru.vladsaybulin.shikimori

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import ru.vladsaybulin.core.auth.AuthorizationLauncher
import ru.vladsaybulin.core.auth.ShikimoriAuthState
import ru.vladsaybulin.core.auth.registerAuthorizationLauncher
import ru.vladsaybulin.shikimori.ui.App
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var authState: ShikimoriAuthState

    private lateinit var authorizationLauncher: AuthorizationLauncher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authorizationLauncher = registerAuthorizationLauncher(authState)

        //if (!authState.isAuthorized) {
        //    authorizationLauncher.launch()
        //}

        setContent {
            App()
        }
    }
}
