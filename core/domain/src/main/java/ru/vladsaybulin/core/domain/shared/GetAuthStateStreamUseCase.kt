package ru.vladsaybulin.core.domain.shared

import kotlinx.coroutines.flow.StateFlow
import ru.vladsaybulin.core.auth.ShikimoriAuthorization
import ru.vladsaybulin.model.auth.ShikimoriAuthState
import javax.inject.Inject

class GetAuthStateStreamUseCase @Inject constructor(private val auth: ShikimoriAuthorization) {
    operator fun invoke(): StateFlow<ShikimoriAuthState> = auth.shikimoriAuthState
}