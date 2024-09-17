package ru.vladsaybulin.feature.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.vladsaybulin.core.domain.profile.GetBriefUserStreamUseCase
import ru.vladsaybulin.data.repository.UserRepository
import ru.vladsaybulin.feature.profile.navigation.ProfileScreenRoute
import ru.vladsaybulin.model.user.BriefUser
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getBriefUserStreamUseCase: GetBriefUserStreamUseCase
) : ViewModel() {

    private val route = savedStateHandle.toRoute<ProfileScreenRoute>()

    val state = getBriefUserStreamUseCase(route.userId).map { user ->
        if (user == null) {
            ProfileUiState.NotAuthorized
        } else {
            ProfileUiState.Success(user)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileUiState.Loading
    )

}

sealed class ProfileUiState {
    data object NotAuthorized : ProfileUiState()

    data object Loading : ProfileUiState()

    class Success(val user: BriefUser) : ProfileUiState()
}