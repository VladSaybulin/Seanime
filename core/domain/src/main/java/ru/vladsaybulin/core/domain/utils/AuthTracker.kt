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

package ru.vladsaybulin.core.domain.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.vladsaybulin.common.network.di.ApplicationScope
import ru.vladsaybulin.core.auth.ShikimoriAuthorization
import ru.vladsaybulin.model.auth.ShikimoriAuthState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AuthTracker @Inject constructor(
    shikimoriAuthorization: ShikimoriAuthorization,
    @ApplicationScope appScope: CoroutineScope
) {

    private val observers = mutableListOf<Observer>()

    init {
        shikimoriAuthorization.shikimoriAuthState
            .onEach { notifyObservers(it) }
            .launchIn(appScope)
    }

    fun addObserver(observer: Observer) {
        synchronized(observers) {
            observers.add(observer)
        }
    }

    fun removeObserver(observer: Observer) {
        synchronized(observers) {
            observers.remove(observer)
        }
    }

    private fun notifyObservers(state: ShikimoriAuthState) {
        synchronized(observers) {
            observers.forEach { it.onAuthStateChanged(state) }
        }
    }

    fun interface Observer {
        fun onAuthStateChanged(state: ShikimoriAuthState)
    }
}