package ru.vladsaybulin.shikimori

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import dagger.hilt.android.AndroidEntryPoint
import ru.vladsaybulin.core.auth.AuthorizationActivityResultCallback
import ru.vladsaybulin.core.auth.ShikimoriAuthState
import ru.vladsaybulin.core.auth.createAuthorizationService
import ru.vladsaybulin.core.auth.startAuthorizationActivity
import ru.vladsaybulin.shikimori.ui.App
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var authState: ShikimoriAuthState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authorizationService = createAuthorizationService()
        val authLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            AuthorizationActivityResultCallback(authState, authorizationService)
        )

        //if (!authState.isAuthorized) {
        //    startAuthorizationActivity(authLauncher, authorizationService)
        //}


        setContent {
            App()
        }
    }
}
