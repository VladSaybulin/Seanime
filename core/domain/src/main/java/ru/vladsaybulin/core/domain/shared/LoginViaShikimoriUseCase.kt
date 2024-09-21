package ru.vladsaybulin.core.domain.shared

import ru.vladsaybulin.core.auth.ShikimoriAuthorization
import javax.inject.Inject

class LoginViaShikimoriUseCase @Inject constructor(
    private val shikimoriAuthorization: ShikimoriAuthorization
) {
    operator fun invoke() {
        shikimoriAuthorization.login()
    }
}