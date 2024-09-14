package ru.vladsaybulin.core.auth

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import dagger.Lazy
import javax.inject.Inject

class ShikimoriLoginAction @Inject internal constructor(
    private val shikimoriAuthorization: ShikimoriAuthorization,
    private val contract: Lazy<ShikimoriAuthorizationContract>
) {
    private var launcher: ActivityResultLauncher<Unit>? = null

    fun register(activity: ComponentActivity) {
        launcher = activity.registerForActivityResult(contract.get()) { result ->
            if (result == null) return@registerForActivityResult

            val (response, exception) = result

            if (response != null) {
                shikimoriAuthorization.logIn(response)
            } else {
                shikimoriAuthorization.authorizationFailed(checkNotNull(exception))
            }
        }
    }

    operator fun invoke() {
        checkNotNull(launcher) { "Launcher in unregistered" }.launch(Unit)
    }
}