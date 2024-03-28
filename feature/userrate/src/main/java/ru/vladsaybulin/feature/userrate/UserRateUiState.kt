package ru.vladsaybulin.feature.userrate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ru.vladsaybulin.model.UserRate
import ru.vladsaybulin.model.UserRateStatus

@Composable
fun rememberUserRateUiState(userRate: UserRate) = remember {
    UserRateUiState(
        initialStatus = userRate.status,
        initialScore = userRate.score,
        initialEpisodes = userRate.episodes,
        initialChapters = userRate.chapters,
        initialVolumes = userRate.volumes,
        initialText = userRate.text
    )
}

class UserRateUiState(
    initialStatus: UserRateStatus,
    initialScore: Int,
    initialEpisodes: Int,
    initialChapters: Int,
    initialVolumes: Int,
    initialText: String
) {

    var status by mutableStateOf(initialStatus)
    var score by mutableIntStateOf(initialScore)
    var episodes by mutableIntStateOf(initialEpisodes)
    var chapters by mutableIntStateOf(initialChapters)
    var volumes by mutableIntStateOf(initialVolumes)
    var text by mutableStateOf(initialText)
}