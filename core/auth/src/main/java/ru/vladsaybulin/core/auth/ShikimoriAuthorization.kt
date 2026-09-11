/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vladsaybulin.core.auth

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import dagger.Lazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.vladsaybulin.common.network.di.ApplicationScope
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Responsible only for the OAuth browser-redirect phase.
 *
 * Emits [AuthCodeResult] via [codeResults] when the OS returns from the
 * Shikimori authorization page. All token management is delegated to
 * [SessionManager] which consumes this flow.
 */
@Singleton
class ShikimoriAuthorization @Inject internal constructor(
    private val contract: Lazy<ShikimoriAuthorizationContract>,
    @ApplicationScope private val appScope: CoroutineScope
) {
    private val _codeResults = MutableSharedFlow<AuthCodeResult>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    /** Hot shared flow; [SessionManager] is the sole subscriber. */
    val codeResults: SharedFlow<AuthCodeResult> = _codeResults.asSharedFlow()

    private var launcher: ActivityResultLauncher<Unit>? = null

    /** Must be called from [ComponentActivity.onCreate] before any [login] call. */
    fun registerLoginAction(activity: ComponentActivity) {
        launcher = activity.registerForActivityResult(contract.get()) { result ->
            if (result == null) return@registerForActivityResult

            val authCode = result.response?.authorizationCode
            val exception = result.exception

            val codeResult: AuthCodeResult = when {
                authCode != null -> AuthCodeResult.Success(authCode)
                exception != null -> AuthCodeResult.Failure(exception)
                else -> return@registerForActivityResult
            }

            appScope.launch { _codeResults.emit(codeResult) }
        }
    }

    fun login() {
        checkNotNull(launcher) {
            "registerLoginAction() must be called before login()"
        }.launch(Unit)
    }
}
