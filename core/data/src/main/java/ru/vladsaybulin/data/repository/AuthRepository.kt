package ru.vladsaybulin.data.repository

import kotlinx.coroutines.flow.StateFlow
import ru.vladsaybulin.core.auth.ShikimoriAuthorization
import ru.vladsaybulin.model.auth.ShikimoriAuthState
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authorization: ShikimoriAuthorization
) {
    val authState: StateFlow<ShikimoriAuthState> = authorization.shikimoriAuthState

    suspend fun signOut() {
        authorization.signOut()
    }
}