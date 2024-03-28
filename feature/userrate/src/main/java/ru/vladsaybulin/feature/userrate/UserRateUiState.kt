package ru.vladsaybulin.feature.userrate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ru.vladsaybulin.feature.userrate.components.CounterState
import ru.vladsaybulin.feature.userrate.components.ScoreRowState
import ru.vladsaybulin.feature.userrate.components.rememberScoreRowState
import ru.vladsaybulin.model.UserRateStatus

@Composable
fun rememberUserRateUiState(setup: UserRateSetup): UserRateUiState {
    val scoreState = rememberScoreRowState(initialScore = setup.userRate.score)
    return when (setup) {
        is UserRateSetup.AnimeUserRate -> rememberAnimeUserRateUiState(
            setup = setup,
            scoreState = scoreState
        )
    }
}

@Composable
fun rememberAnimeUserRateUiState(
    setup: UserRateSetup.AnimeUserRate,
    scoreState: ScoreRowState
) = remember {
    UserRateUiState(
        initialStatus = setup.userRate.status,
        scoreState = scoreState,
        episodesCounterState = CounterState(
            initialCount = setup.userRate.episodes,
            range = 0..setup.maxEpisodes
        ),
        chaptersCounterState = null,
        volumesCounterState = null,
        initialText = setup.userRate.text
    )
}

class UserRateUiState(
    initialStatus: UserRateStatus,
    val scoreState: ScoreRowState,
    val episodesCounterState: CounterState?,
    val chaptersCounterState: CounterState?,
    val volumesCounterState: CounterState?,
    initialText: String,
) {
    var status by mutableStateOf(initialStatus)
    var text by mutableStateOf(initialText)
}