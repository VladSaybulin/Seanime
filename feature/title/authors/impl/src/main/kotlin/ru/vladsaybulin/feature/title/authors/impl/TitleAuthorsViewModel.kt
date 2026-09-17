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

package ru.vladsaybulin.feature.title.authors.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.vladsaybulin.core.domain.GetAuthorsUseCase
import ru.vladsaybulin.feature.title.authors.api.navigation.TitleAuthorsNavKey
import ru.vladsaybulin.model.person.PersonWithRoles

@HiltViewModel(assistedFactory = TitleAuthorsViewModel.Factory::class)
class TitleAuthorsViewModel @AssistedInject constructor(
    getAuthorsUseCase: GetAuthorsUseCase,
    @Assisted key: TitleAuthorsNavKey
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(key: TitleAuthorsNavKey): TitleAuthorsViewModel
    }

    val uiState = getAuthorsUseCase(key.titleType, key.titleId)
        .map { AuthorsUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = AuthorsUiState.Loading
        )
}

sealed class AuthorsUiState {
    data object Loading : AuthorsUiState()

    data class Success(val authors: List<PersonWithRoles>) : AuthorsUiState()
}