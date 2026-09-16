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

package ru.vladsaybulin.feature.profile.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.Lazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.vladsaybulin.core.domain.app.LogoutUseCase
import ru.vladsaybulin.core.domain.profile.GetBriefUserStreamUseCase
import ru.vladsaybulin.core.domain.profile.IsMeUseCase
import ru.vladsaybulin.core.domain.shared.GetAuthStateStreamUseCase
import ru.vladsaybulin.core.domain.shared.LoginViaShikimoriUseCase
import ru.vladsaybulin.feature.profile.api.navigation.ProfileNavKey
import ru.vladsaybulin.model.auth.SessionState
import ru.vladsaybulin.model.user.BriefUser
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    getBriefUserStreamUseCase: GetBriefUserStreamUseCase,
    isMeUseCase: IsMeUseCase,
    getAuthStateStreamUseCase: GetAuthStateStreamUseCase,
    private val logoutUseCase: Lazy<LogoutUseCase>,
    private val loginViaShikimoriUseCase: Lazy<LoginViaShikimoriUseCase>,
    @Assisted key: ProfileNavKey
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(key: ProfileNavKey): ProfileViewModel
    }

    val isMe = when (val id = key.userId) {
        null -> getAuthStateStreamUseCase().map { it == SessionState.Authenticated }
        else -> isMeUseCase(id)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val state = getBriefUserStreamUseCase(key.userId).map { user ->
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