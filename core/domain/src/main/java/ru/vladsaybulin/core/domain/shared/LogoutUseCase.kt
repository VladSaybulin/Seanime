package ru.vladsaybulin.core.domain.shared

import ru.vladsaybulin.common.auth.LogoutAction
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val logoutAction: LogoutAction
) {
    suspend operator fun invoke() {
        logoutAction.logout()
    }
}