package ru.vladsaybulin.core.auth

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import dagger.Lazy
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication
import javax.inject.Inject

class ShikimoriLoginAction @Inject internal constructor(
    private val shikimoriAuthorization: ShikimoriAuthorization,
    private val contract: Lazy<ShikimoriAuthorizationContract>,
    private val service: Lazy<AuthorizationService>,
    private val client: ClientAuthentication
) {
    private var launcher: ActivityResultLauncher<Unit>? = null

    fun register(activity: ComponentActivity) {
        launcher = activity.registerForActivityResult(contract.get()) { result ->
            if (result == null) return@registerForActivityResult

            val (response, exception) = result
            val newAuthState = AuthState().apply { update(response, exception) }

            if (response != null) {
                val request = response.createTokenExchangeRequest()
                service.get().performTokenRequest(request, client) { tokenResponse, _ ->
                    if (tokenResponse != null) {
                        newAuthState.update(tokenResponse, exception)
                        shikimoriAuthorization.onNewAuthState(newAuthState)
                    }
                }
            } else {
                shikimoriAuthorization.onNewAuthState(null)
            }
        }
    }

    operator fun invoke() {
        checkNotNull(launcher) { "Launcher in unregistered" }.launch(Unit)
    }
}