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

package ru.vladsaybulin.feature.rate.editor.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.Lazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.vladsaybulin.core.domain.rate.Progress
import ru.vladsaybulin.core.domain.rate.UserRateCorrector
import ru.vladsaybulin.core.domain.repository.UserRateRepository
import ru.vladsaybulin.feature.rate.editor.api.navigation.RateEditorNavKey
import ru.vladsaybulin.model.userrate.UserRateContext
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.UserRateValues

@HiltViewModel(assistedFactory = RateEditorViewModel.Factory::class)
class RateEditorViewModel @AssistedInject constructor(
    correctorFactory: UserRateCorrector.Factory,
    private val userRateRepository: Lazy<UserRateRepository>,
    @Assisted private val key: RateEditorNavKey
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(key: RateEditorNavKey): RateEditorViewModel
    }

    /**
     * Represents the current state of the data loading process.
     */
    sealed class LoadState {
        object Loading : LoadState()
        data class Success(
            val rateValues: UserRateValues,
            val context: UserRateContext,
            val availableStatuses: List<UserRateStatus>
        ) : LoadState()
    }

    /**
     * Represents the different UI effects that can occur.
     */
    enum class UiEffect {
        Idle, Saving, Deleting, Finished
    }

    /**
     * Represents the changes that need to be forced in the UI based on user actions.
     */
    sealed class ForceChanges {
        data class Status(val status: UserRateStatus) : ForceChanges()
        data class Progression(val progress: Progress) : ForceChanges()
    }

    val titleType = key.titleReference.titleType

    private val corrector = correctorFactory.create(
        titleType = key.titleReference.titleType,
        titleId = key.titleReference.titleId,
        loadedContext = key.context.data
    )

    /**
     * Indicates whether the corrector has been initialized. This is a state flow that emits `true`
     * when the corrector is initialized and `false` otherwise. The initialization process involves loading the
     * context if it was not provided.
     */
    val correctorInitialized = flow {
        corrector.initialize()
        emit(true)
    }.stateIn(viewModelScope, started = WhileSubscribed(5000), initialValue = false)

    /**
     * Represents the current load state of the user rate data. It is a state flow that
     * emits either a [LoadState.Loading] or a [LoadState.Success] object.
     * The initial value is determined by checking if the data can be extracted from the provided key.
     * If the data is available, it emits a [LoadState.Success] object; otherwise, it starts loading the data stream and
     * emits a [LoadState.Loading] object until the data is loaded.
     */
    val loadState = extractLoadedDataFromKey()?.let(::MutableStateFlow)
        ?: loadDataStream().stateIn(viewModelScope, started = WhileSubscribed(5000), initialValue = LoadState.Loading)

    /**
     * A shared flow that emits [ForceChanges] events provided by the corrector.
     */
    private val _forceChanges = MutableSharedFlow<ForceChanges>(extraBufferCapacity = 1)
    val forceChanges = _forceChanges.asSharedFlow()

    /**
     * A shared flow that emits [UiEffect] events to notify the UI about saving, deleting, or finishing actions.
     */
    private val _uiEffect = MutableSharedFlow<UiEffect>(extraBufferCapacity = 1)
    val uiEffect: Flow<UiEffect> = _uiEffect.asSharedFlow()

    /**
     * Handles the event when the user rate status changes. It calls the corrector to determine if any progress changes are needed.
     * If progress changes are returned by the corrector, it emits a [ForceChanges.Progression] event to notify the UI about the required changes.
     * @param prevStatus The previous user rate status.
     * @param newStatus The new user rate status.
     */
    fun onStatusChangeTriggered(prevStatus: UserRateStatus, newStatus: UserRateStatus) {
        corrector.onStatusChanged(prevStatus, newStatus)?.let {
            _forceChanges.tryEmit(ForceChanges.Progression(it))
        }
    }

    /**
     * Handles the event when the user changes the progress of their rate. It calls the corrector to determine if any status changes are needed.
     * If status changes are returned by the corrector, it emits a [ForceChanges.Status] event to notify the UI about the required changes.
     * @param currentStatus The current user rate status.
     * @param progress The new progress values.
     */
    fun onProgressChangeTriggered(currentStatus: UserRateStatus, progress: Progress) {
        corrector.onProgressChanged(currentStatus, progress)?.let {
            _forceChanges.tryEmit(ForceChanges.Status(it))
        }
    }

    /**
     * Handles the event when the user clicks the save button.
     * It emits a [UiEffect.Saving] event to notify the UI that the saving process has started.
     * It then either updates an existing user rate or creates a new one based on the provided [UserRateValues].
     * After the operation is complete, it emits a [UiEffect.Finished] event to notify the UI that the saving process has finished.
     * @param newValues The new user rate values to be saved.
     */
    fun onSaveClick(newValues: UserRateValues) {
        viewModelScope.launch {
            _uiEffect.tryEmit(UiEffect.Saving)

            key.rateId?.let { id ->
                userRateRepository.get().updateUserRate(
                    userRateId = id,
                    userRateValues = newValues
                )
            } ?: userRateRepository.get().createUserRate(
                entryType = key.titleReference.titleType,
                entryId = key.titleReference.titleId,
                userRateValues = newValues
            )

            _uiEffect.tryEmit(UiEffect.Finished)
        }
    }

    /**
     * Handles the event when the user clicks the delete button.
     * It emits a [UiEffect.Deleting] event to notify the UI that the deletion process has started.
     * It then deletes the existing user rate based on the provided rate ID.
     * After the operation is complete, it emits a [UiEffect.Finished] event to notify the UI that the deletion process has finished.
     */
    fun onDeleteClick() {
        viewModelScope.launch {
            _uiEffect.tryEmit(UiEffect.Deleting)

            key.rateId?.let { id ->
                userRateRepository.get().deleteUserRate(id)
            }

            _uiEffect.tryEmit(UiEffect.Finished)
        }
    }

    /**
     * Attempts to extract the loaded data from the provided key. If the context and rate values are available,
     * it returns a [LoadState.Success] object containing the extracted data.
     * If `key.rateId` is null, we request new rate values from the [corrector].
     * If the context or rate values are not available, it returns null.
     * @return A [LoadState.Success] object if the data is available, or null if the data is not available.
     */
    private fun extractLoadedDataFromKey(): LoadState.Success? {
        val context = key.context.data ?: return null
        val rateValues = if (key.rateId != null) {
            key.initialRateValues.data ?: return null
        } else corrector.newUserRateValues()

        return LoadState.Success(
            rateValues = rateValues,
            context = context,
            availableStatuses = corrector.availableStatuses // Corrector should already be outputting the correct data.
        )
    }

    /**
     * Loads the user rate data stream by combining the values flow and the corrector initialization flow.
     * It emits a [LoadState.Success] object containing the loaded rate values, context, and available statuses when both flows emit their values.
     * If the rate values are not available or `key.rateId` is null, it creates new user rate values using the [corrector].
     * @return A flow of [LoadState] objects representing the current load state of the user rate data.
     */
    private fun loadDataStream(): Flow<LoadState> {
        val valuesFlow = flow {
            val values = key.rateId?.let { userRateRepository.get().getUserRateValues(it) }
            emit(values)
        }

        val correctorFlow = correctorInitialized.filter { it }

        return combine(
            valuesFlow,
            correctorFlow
        ) { values, _ ->
            LoadState.Success(
                rateValues = values ?: corrector.newUserRateValues(),
                context = corrector.context,
                availableStatuses = corrector.availableStatuses
            )
        }
    }
}

