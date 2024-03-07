package ru.vladsaybulin.feature.details

import ru.vladsaybulin.feature.details.model.Details
import ru.vladsaybulin.feature.details.model.SimilarEntry
import ru.vladsaybulin.model.UserRate

sealed class DetailsUiState {
    data object Loading : DetailsUiState()

    data class Error(val throwable: Throwable) : DetailsUiState()

    data class Success(
        val details: Details,
        val userRate: UserRate?,
        val similar: List<SimilarEntry>,
    ) : DetailsUiState()
}