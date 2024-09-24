package ru.vladsaybulin.feature.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.vladsaybulin.core.domain.profile.GetBriefUserStreamUseCase
import ru.vladsaybulin.core.domain.profile.IsMeUseCase
import ru.vladsaybulin.core.domain.shared.GetAuthStateStreamUseCase
import ru.vladsaybulin.core.domain.shared.LoginViaShikimoriUseCase
import ru.vladsaybulin.core.domain.shared.LogoutUseCase
import ru.vladsaybulin.feature.profile.navigation.ProfileScreenRoute
import ru.vladsaybulin.model.auth.ShikimoriAuthState
import ru.vladsaybulin.model.user.BriefUser
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getBriefUserStreamUseCase: GetBriefUserStreamUseCase,
    isMeUseCase: IsMeUseCase,
    getAuthStateStreamUseCase: GetAuthStateStreamUseCase,
    private val logoutUseCase: Lazy<LogoutUseCase>,
    private val loginViaShikimoriUseCase: Lazy<LoginViaShikimoriUseCase>
) : ViewModel() {

    private val route = savedStateHandle.toRoute<ProfileScreenRoute>()

    val isMe = when (route.userId) {
        null -> getAuthStateStreamUseCase().map { it == ShikimoriAuthState.LOGGED_IN }
        else -> isMeUseCase(route.userId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val state = getBriefUserStreamUseCase(route.userId).map { user ->
        when (user) {
            null -> ProfileUiState.NotAuthorized
            else -> ProfileUiState.Success(user)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileUiState.Loading
    )

    fun loginViaShikimori() {
        loginViaShikimoriUseCase.get().invoke()
    }

    fun logout() {
        viewModelScope.launch { logoutUseCase.get().invoke() }
    }

}

sealed class ProfileUiState {
    data object NotAuthorized : ProfileUiState()

    data object Loading : ProfileUiState()

    class Success(val user: BriefUser) : ProfileUiState()
}