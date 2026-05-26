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

package ru.vladsaybulin.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import ru.vladsaybulin.common.ui.tryRefresh
import ru.vladsaybulin.core.domain.calendar.CalendarDay
import ru.vladsaybulin.core.domain.calendar.GetCalendarDaysUseCase
import ru.vladsaybulin.core.domain.calendar.RefreshCalendarUseCase
import ru.vladsaybulin.data.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    getCalendarDaysUseCase: GetCalendarDaysUseCase,
    private val refreshCalendarUseCase: RefreshCalendarUseCase,
    userRepository: UserRepository,
) : ViewModel() {

    val me = userRepository.getMeStream()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val uiState: StateFlow<CalendarUiState> = getCalendarDaysUseCase()
        .onStart { internalRefresh(false) }
        .map { CalendarUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CalendarUiState.Loading
        )

    suspend fun refresh() {
       internalRefresh(true)
    }

    suspend fun internalRefresh(forceRefresh: Boolean) {
        tryRefresh(
            catch = { },
        ) {
            refreshCalendarUseCase(forceRefresh)
        }
    }
}

sealed class CalendarUiState {
    data object Loading : CalendarUiState()
    data class Success(val calendarDays: List<CalendarDay>) : CalendarUiState()
}