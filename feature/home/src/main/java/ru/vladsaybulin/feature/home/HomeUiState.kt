package ru.vladsaybulin.feature.home

import kotlinx.collections.immutable.ImmutableList
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.topic.Topic
import ru.vladsaybulin.model.user.BriefUser
import ru.vladsaybulin.model.userrate.UserRateWithEntry
import javax.annotation.concurrent.Immutable

@Immutable
sealed class HomeUiState {
    data object Loading : HomeUiState()

    @Immutable
    data class Error(val throwable: Throwable) : HomeUiState()

    @Immutable
    data class Success(
        val inProgressUserRates: ImmutableList<UserRateWithEntry>,
        val ongoings: ImmutableList<Anime>,
        val newsTopics: ImmutableList<Topic>,
        val me: BriefUser?
    ) : HomeUiState()
}