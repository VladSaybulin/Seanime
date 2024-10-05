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