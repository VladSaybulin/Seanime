package ru.vladsaybulin.feature.userrate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import ru.vladsaybulin.feature.userrate.CounterState.Companion.UNLIMITED_LIMIT
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryStatus.Anons
import ru.vladsaybulin.model.userrate.EditableUserRate
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.UserRateStatus.Completed
import ru.vladsaybulin.model.userrate.UserRateStatus.Dropped
import ru.vladsaybulin.model.userrate.UserRateStatus.OnHold
import ru.vladsaybulin.model.userrate.UserRateStatus.Planned
import ru.vladsaybulin.model.userrate.UserRateStatus.Rewatching
import ru.vladsaybulin.model.userrate.UserRateStatus.Watching
import ru.vladsaybulin.model.userrate.UserRateValues

@Composable
fun EditableUserRate.asState(): UserRateState {
    val state = remember(this) {
        UserRateState(
            availableStatuses = getAvailableUserRateStatuses(userRate.status, entryStatus),
            initialUserRateStatus = userRate.status,
            progressCounterStates = createProgressCounterStates(
                initialEpisodes = userRate.episodes,
                maxEpisodes = maxEpisodes,
                initialChapters = userRate.chapters,
                maxChapters = maxChapters,
                initialVolumes = userRate.volumes,
                maxVolumes = maxVolumes
            ),
            rewatchesCounterState = CounterState(userRate.rewatches),
            initialScore = userRate.score,
            initialText = userRate.text,
            isEnded = entryStatus == EntryStatus.Released || entryStatus == EntryStatus.Discontinued
        )
    }

    val scope = rememberCoroutineScope()
    LaunchedEffect(state, scope) {
        state.collectProgressCounterStates(scope)
    }

    return state
}

enum class ProgressCounterType {
    Episodes, Chapters, Volumes
}

class UserRateState(
    val availableStatuses: ImmutableList<UserRateStatus>,
    initialUserRateStatus: UserRateStatus,
    val progressCounterStates: Map<ProgressCounterType, CounterState>,
    val rewatchesCounterState: CounterState,
    initialScore: Int,
    initialText: String,
    private val isEnded: Boolean
) {
    private val _status = mutableStateOf(initialUserRateStatus)
    val status
        get() = _status.value

    private val _score = mutableIntStateOf(initialScore)
    val score
        get() = _score.intValue

    private val _text = mutableStateOf(initialText)
    val text
        get() = _text.value

    val progressCounterEnabled by derivedStateOf { _status.value in InProgressStatuses }

    fun onStatusChanged(newStatus: UserRateStatus) {
        _status.value = newStatus

        if (newStatus == Completed) {
            progressCounterStates.forEach { (_, counterState) -> counterState.setMaxCount() }
        }

        if (newStatus == Rewatching) {
            progressCounterStates.forEach { (_, counterState) -> counterState.setZeroCount() }
        }
    }

    fun onScoreChanged(newScore: Int) {
        check(newScore in 0..10)
        _score.intValue = newScore
    }

    fun onTextChanger(newText: String) {
        _text.value = newText
    }

    private fun onCounterLimitReached(type: ProgressCounterType) {
        if (!isEnded || _status.value in InProgressStatuses) return

        progressCounterStates.forEach { (counterType, counterState) ->
            if (counterType == type) return@forEach
            counterState.setMaxCount()
        }

        if (_status.value == Rewatching) {
            rewatchesCounterState.onIncrement()
        }
        _status.value = Completed
    }

    fun collectProgressCounterStates(scope: CoroutineScope) {
        progressCounterStates.forEach { (type, state) ->
            if (state.isUnlimitedLimit()) return@forEach
            val flow = snapshotFlow { state.value }.filter { it == state.value }
            scope.launch {
                flow.collect { onCounterLimitReached(type) }
            }
        }
    }
}

fun CounterState.setMaxCount() {
    if (isUnlimitedLimit()) return
    value = limit
}

fun CounterState.setZeroCount() {
    value = 0
}

private fun CounterState.isUnlimitedLimit() = limit == UNLIMITED_LIMIT

private fun createProgressCounterStates(
    initialEpisodes: Int,
    maxEpisodes: Int,
    initialChapters: Int,
    maxChapters: Int,
    initialVolumes: Int,
    maxVolumes: Int
): Map<ProgressCounterType, CounterState> = buildMap {
    if (maxEpisodes != -1) {
        put(
            key = ProgressCounterType.Episodes,
            value = CounterState(
                initialCount = initialEpisodes,
                limit = getLimit(maxEpisodes)
            )
        )
    }

    if (maxChapters != -1) {
        put(
            key = ProgressCounterType.Chapters,
            value = CounterState(
                initialCount = initialChapters,
                limit = getLimit(maxChapters)
            )
        )
    }

    if (maxVolumes != -1) {
        put(
            key = ProgressCounterType.Volumes,
            value = CounterState(
                initialCount = initialVolumes,
                limit = getLimit(maxVolumes)
            )
        )
    }
}

private fun getLimit(limit: Int) = if (limit == 0) UNLIMITED_LIMIT else limit

private fun getAvailableUserRateStatuses(
    initialUserRateStatus: UserRateStatus,
    entryStatus: EntryStatus
) = buildList {
    add(Planned)
    if (entryStatus != Anons) {
        add(Watching)
        add(Rewatching)
    }
    if (entryStatus == EntryStatus.Released) {
        add(Completed)
    }
    add(OnHold)
    add(Dropped)
}.let {
    if (initialUserRateStatus in it) it else AllUserRateStatuses
}.toImmutableList()

fun UserRateState.toUserRateValues() = UserRateValues(
    status = status,
    score = score,
    episodes = progressCounterStates[ProgressCounterType.Episodes]?.value ?: 0,
    chapters = progressCounterStates[ProgressCounterType.Chapters]?.value ?: 0,
    volumes = progressCounterStates[ProgressCounterType.Volumes]?.value ?: 0,
    rewatches = rewatchesCounterState.value,
    text = text
)

private val AllUserRateStatuses
    get() = listOf(Planned, Watching, Rewatching, Completed, OnHold, Dropped)

private val InProgressStatuses = listOf(Watching, Rewatching)