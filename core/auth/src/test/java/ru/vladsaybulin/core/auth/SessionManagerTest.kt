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
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import ru.vladsaybulin.datastore.SeanimePreferencesDataSource
import ru.vladsaybulin.model.auth.SessionState

@OptIn(ExperimentalCoroutinesApi::class)
class SessionManagerTest {

    @Test
    fun `logout during refresh does not restore tokens`() = runTest {
        val tokenStore = mock<AuthTokenStore>()
        whenever(tokenStore.getTokens()).thenReturn(Result.success(EXPIRED_TOKENS))
        whenever(tokenStore.clearTokens()).thenReturn(Unit)

        val prefs = mock<SeanimePreferencesDataSource>()
        whenever(prefs.myId).thenReturn(flowOf(null))
        whenever(prefs.setMyId(null)).thenReturn(Unit)

        val auth = mock<ShikimoriAuthorization>()
        whenever(auth.codeResults).thenReturn(MutableSharedFlow())
        val logoutCleaner = CountingLogoutCleaner()
        val refreshStarted = CompletableDeferred<Unit>()
        val allowRefresh = CompletableDeferred<Unit>()
        val tokenGateway = GateTokenGateway(refreshStarted, allowRefresh, REFRESHED_TOKENS)

        val sessionManager = SessionManager(
            authorization = auth,
            tokenGateway = lazyOf(tokenGateway),
            tokenStore = tokenStore,
            preferencesDataSource = prefs,
            userIdFetcher = lazyOf { 100L },
            onLogoutCleaner = lazyOf(logoutCleaner),
            appScope = backgroundScope
        )

        val refreshJob = async { sessionManager.getFreshToken() }
        refreshStarted.await()

        sessionManager.logout()
        allowRefresh.complete(Unit)

        assertNull(refreshJob.await())
        assertEquals(SessionState.LoggedOut, sessionManager.sessionState.value)
        assertEquals(1, logoutCleaner.calls)

        verify(tokenStore, never()).saveTokens(REFRESHED_TOKENS)
        verify(tokenStore).clearTokens()
        verify(prefs).setMyId(null)
    }

    @Test
    fun `logout during user id resolve keeps logged out state`() = runTest {
        val tokenStore = mock<AuthTokenStore>()
        whenever(tokenStore.getTokens()).thenReturn(Result.success(VALID_TOKENS))
        whenever(tokenStore.clearTokens()).thenReturn(Unit)

        val prefs = mock<SeanimePreferencesDataSource>()
        whenever(prefs.myId).thenReturn(flowOf(null))
        whenever(prefs.setMyId(null)).thenReturn(Unit)
        whenever(prefs.setMyId(777L)).thenReturn(Unit)

        val auth = mock<ShikimoriAuthorization>()
        whenever(auth.codeResults).thenReturn(MutableSharedFlow())
        val logoutCleaner = CountingLogoutCleaner()
        val resolveStarted = CompletableDeferred<Unit>()
        val allowResolve = CompletableDeferred<Unit>()
        val userIdFetcher = GateUserIdFetcher(resolveStarted, allowResolve, 777L)

        val sessionManager = SessionManager(
            authorization = auth,
            tokenGateway = lazyOf(NoOpTokenGateway),
            tokenStore = tokenStore,
            preferencesDataSource = prefs,
            userIdFetcher = lazyOf(userIdFetcher),
            onLogoutCleaner = lazyOf(logoutCleaner),
            appScope = backgroundScope
        )

        val resolveJob = async { sessionManager.getUserId() }
        resolveStarted.await()

        sessionManager.logout()
        allowResolve.complete(Unit)

        assertNull(resolveJob.await())
        assertEquals(SessionState.LoggedOut, sessionManager.sessionState.value)
        assertEquals(1, logoutCleaner.calls)

        verify(prefs, never()).setMyId(777L)
    }

    private fun <T> lazyOf(value: T): Lazy<T> = Lazy<T> { value }

    private fun lazyOf(fetchUserId: suspend () -> Long?): Lazy<UserIdFetcher> =
        lazyOf(UserIdFetcher { fetchUserId() })

    private class CountingLogoutCleaner : OnLogoutCleaner {
        var calls: Int = 0

        override suspend fun onLogout() {
            calls++
        }
    }

    private class GateTokenGateway(
        private val refreshStarted: CompletableDeferred<Unit>,
        private val allowRefresh: CompletableDeferred<Unit>,
        private val refreshedTokens: StoredTokens
    ) : TokenExchangeGateway {
        override suspend fun exchange(code: String): StoredTokens = error("Not used in this test")

        override suspend fun refresh(refreshToken: String): StoredTokens {
            refreshStarted.complete(Unit)
            allowRefresh.await()
            return refreshedTokens
        }
    }

    private class GateUserIdFetcher(
        private val started: CompletableDeferred<Unit>,
        private val allowResolve: CompletableDeferred<Unit>,
        private val result: Long
    ) : UserIdFetcher {
        override suspend fun fetchUserId(): Long {
            started.complete(Unit)
            allowResolve.await()
            return result
        }
    }

    private data object NoOpTokenGateway : TokenExchangeGateway {
        override suspend fun exchange(code: String): StoredTokens = error("Not used in this test")

        override suspend fun refresh(refreshToken: String): StoredTokens = error("Not used in this test")
    }

    private companion object {
        val EXPIRED_TOKENS = StoredTokens(
            accessToken = "access-old",
            refreshToken = "refresh-old",
            expiresAtMs = 0L
        )

        val REFRESHED_TOKENS = StoredTokens(
            accessToken = "access-new",
            refreshToken = "refresh-new",
            expiresAtMs = Long.MAX_VALUE
        )

        val VALID_TOKENS = StoredTokens(
            accessToken = "access-valid",
            refreshToken = "refresh-valid",
            expiresAtMs = Long.MAX_VALUE
        )
    }
}



