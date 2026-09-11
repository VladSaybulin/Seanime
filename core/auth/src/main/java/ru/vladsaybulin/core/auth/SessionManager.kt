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

import dagger.Lazy
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.vladsaybulin.common.network.di.ApplicationScope
import ru.vladsaybulin.datastore.SeanimePreferencesDataSource
import ru.vladsaybulin.model.auth.SessionState
import java.io.IOException
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(FlowPreview::class)
@Singleton
class SessionManager @Inject constructor(
    private val authorization: ShikimoriAuthorization,
    private val tokenGateway: Lazy<TokenExchangeGateway>,
    private val tokenStore: AuthTokenStore,
    private val preferencesDataSource: SeanimePreferencesDataSource,
    private val userIdFetcher: Lazy<UserIdFetcher>,
    private val onLogoutCleaner: Lazy<OnLogoutCleaner>,
    @ApplicationScope private val appScope: CoroutineScope
) {
    private val _sessionState = MutableStateFlow<SessionState>(SessionState.LoggedOut)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    private val _userId = MutableStateFlow<Long?>(null)

    /** Completes after the initial restore attempt, even if it failed. */
    private val startupJob = appScope.async { restoreSession() }

    private val refreshMutex = Mutex()
    private var ongoingRefresh: Deferred<String?>? = null

    private val resolveUserIdMutex = Mutex()
    private var ongoingResolveUserId: Deferred<Long?>? = null

    // Guards mutable session writes and marks in-flight results as stale after logout.
    private val sessionMutationMutex = Mutex()
    private val sessionEpoch = AtomicLong(0L)

    init {
        authorization.codeResults
            .onEach { handleAuthCodeResult(it) }
            .launchIn(appScope)
    }

    fun login() = authorization.login()

    /**
     * Returns userId, resolving it lazily if needed.
     *
     * - LoggedOut -> null
     * - IOException/other non-auth failures are propagated to caller
     */
    suspend fun getUserId(): Long? {
        startupJob.await()
        return resolveUserIdIfNeeded()
    }

    /**
     * Lazy reactive stream for current userId.
     *
     * - Emits null when logged out
     * - Resolves userId on demand when session is not LoggedOut
     * - Propagates IOException/other non-auth failures to collector
     */
    fun userIdStream(): Flow<Long?> =
        sessionState
            .mapLatest { state ->
                if (state == SessionState.LoggedOut) {
                    null
                } else {
                    resolveUserIdIfNeeded()
                }
            }
            .distinctUntilChanged()

    suspend fun getFreshToken(): String? {
        startupJob.await()
        return try {
            getOrStartRefresh().await()
        } catch (_: CancellationException) {
            null
        }
    }

    suspend fun logout() {
        sessionMutationMutex.withLock {
            // Invalidate all in-flight work so stale refresh/resolve cannot write state back.
            sessionEpoch.incrementAndGet()

            ongoingRefresh?.cancel()
            ongoingRefresh = null
            ongoingResolveUserId?.cancel()
            ongoingResolveUserId = null

            tokenStore.clearTokens()
            preferencesDataSource.setMyId(null)
            _userId.value = null
            _sessionState.value = SessionState.LoggedOut

            onLogoutCleaner.get().onLogout()
        }
    }

    private suspend fun restoreSession() {
        val epochAtStart = sessionEpoch.get()
        val storedTokens = getValidTokensOrLogout()
        val persistedId = if (storedTokens != null) preferencesDataSource.myId.first() else null

        sessionMutationMutex.withLock {
            if (epochAtStart != sessionEpoch.get()) return

            if (storedTokens == null) {
                _sessionState.value = SessionState.LoggedOut
                _userId.value = null
                preferencesDataSource.setMyId(null)
            } else {
                _sessionState.value = SessionState.Authenticated
                _userId.value = persistedId
            }
        }
    }

    private suspend fun handleAuthCodeResult(result: AuthCodeResult) {
        when (result) {
            is AuthCodeResult.Success -> {
                val epochAtStart = sessionEpoch.get()
                val tokens = try {
                    tokenGateway.get().exchange(result.code)
                } catch (e: CancellationException) {
                    throw e
                } catch (_: Exception) {
                    _sessionState.value = SessionState.LoggedOut
                    return
                }

                sessionMutationMutex.withLock {
                    if (epochAtStart != sessionEpoch.get()) return@withLock

                    tokenStore.saveTokens(tokens)
                    preferencesDataSource.setMyId(null)
                    _userId.value = null
                    _sessionState.value = SessionState.Authenticated
                }
            }

            is AuthCodeResult.Failure -> {
                _sessionState.value = SessionState.LoggedOut
            }
        }
    }

    private suspend fun resolveUserIdIfNeeded(): Long? {
        if (_sessionState.value == SessionState.LoggedOut) return null

        _userId.value?.let { return it }
        val persistedId = preferencesDataSource.myId.first()
        if (persistedId != null) {
            _userId.value = persistedId
            return persistedId
        }

        return try {
            getOrStartResolveUserId().await()
        } catch (_: CancellationException) {
            null
        }
    }

    private suspend fun getOrStartResolveUserId(): Deferred<Long?> =
        resolveUserIdMutex.withLock {
            val existing = ongoingResolveUserId
            if (existing != null && existing.isActive) return@withLock existing
            appScope.async { doResolveUserId() }.also { ongoingResolveUserId = it }
        }

    private suspend fun doResolveUserId(): Long? {
        val epochAtStart = sessionEpoch.get()

        if (getValidTokensOrLogout() == null) {
            _userId.value = null
            _sessionState.value = SessionState.LoggedOut
            preferencesDataSource.setMyId(null)
            return null
        }

        val resolvedUserId = try {
            userIdFetcher.get().fetchUserId()
        } catch (e: IOException) {
            throw e
        } catch (_: AuthException.Unauthorized) {
            logout()
            return null
        }

        if (resolvedUserId == null) {
            logout()
            return null
        }

        return sessionMutationMutex.withLock {
            if (epochAtStart != sessionEpoch.get() || _sessionState.value == SessionState.LoggedOut) {
                return@withLock null
            }

            _userId.value = resolvedUserId
            preferencesDataSource.setMyId(resolvedUserId)
            _sessionState.value = SessionState.Authenticated
            resolvedUserId
        }
    }

    private suspend fun getOrStartRefresh(): Deferred<String?> =
        refreshMutex.withLock {
            val existing = ongoingRefresh
            if (existing != null && existing.isActive) return@withLock existing
            appScope.async { doRefresh() }.also { ongoingRefresh = it }
        }

    private suspend fun doRefresh(): String? {
        val epochAtStart = sessionEpoch.get()
        val tokens = getValidTokensOrLogout() ?: return null
        if (!tokens.isExpired()) {
            if (epochAtStart != sessionEpoch.get() || _sessionState.value == SessionState.LoggedOut) {
                return null
            }
            return tokens.accessToken
        }

        return try {
            val refreshed = tokenGateway.get().refresh(tokens.refreshToken)
            sessionMutationMutex.withLock {
                if (epochAtStart != sessionEpoch.get() || _sessionState.value == SessionState.LoggedOut) {
                    return@withLock null
                }

                tokenStore.saveTokens(refreshed)
                _sessionState.value = SessionState.Authenticated
                refreshed.accessToken
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            throw e
        } catch (_: AuthException) {
            logout()
            null
        } catch (_: Exception) {
            logout()
            null
        }
    }

    private suspend fun getValidTokensOrLogout(): StoredTokens? {
        val tokens = tokenStore.getTokens()
        if (tokens.isFailure) {
            logout()
            return null
        }
        return tokens.getOrNull()
    }
}
