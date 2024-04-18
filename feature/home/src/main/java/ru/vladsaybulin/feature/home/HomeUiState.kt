package ru.vladsaybulin.feature.home

import kotlinx.collections.immutable.ImmutableList
import ru.vladsaybulin.model.Anime
import ru.vladsaybulin.model.UserRateWithEntry
import ru.vladsaybulin.model.topic.Topic

sealed class HomeUiState {
    data object Loading : HomeUiState()

    data class Error(val throwable: Throwable) : HomeUiState()

    data class Success(
        val inProgressUserRates: ImmutableList<UserRateWithEntry>,
        val ongoings: ImmutableList<Anime>,
        val newsTopics: ImmutableList<Topic>,
    ) : HomeUiState()
}