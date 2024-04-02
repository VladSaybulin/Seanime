package ru.vladsaybulin.feature.userrate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import ru.vladsaybulin.feature.userrate.components.CounterState
import ru.vladsaybulin.feature.userrate.components.ScoreState
import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.model.UserRateStatus
import ru.vladsaybulin.model.UserRateStatus.Completed
import ru.vladsaybulin.model.UserRateStatus.Dropped
import ru.vladsaybulin.model.UserRateStatus.OnHold
import ru.vladsaybulin.model.UserRateStatus.Planned
import ru.vladsaybulin.model.UserRateStatus.Rewatching
import ru.vladsaybulin.model.UserRateStatus.Watching

@Composable
fun StateFlow<UserRateSetup>.collectAsUserRateState(): UserRateState? {
    val setupState = this.collectAsStateWithLifecycle()
    val state = remember(setupState.value) {
        setupState.value.let { setup ->
            when (setup) {
                is UserRateSetup.Success -> setup.asState()
                else -> null
            }
        }
    } ?: return null

    LaunchedEffect(key1 = state) {
        state.collectEpisodesChanges()
    }
    LaunchedEffect(key1 = state) {
        state.collectChaptersChanges()
    }
    LaunchedEffect(key1 = state) {
        state.collectVolumesChanges()
    }
    LaunchedEffect(key1 = state) {
        state.collectRewatchesChanges()
    }
    return state
}

private fun UserRateSetup.Success.asState() = with(userRate) {
    UserRateState(
        enabledAutoCorrect = enabledAutocorrect,
        context = context,
        initialStatus = status,
        initialScore = score,
        initialEpisodes = episodes,
        initialChapters = chapters,
        initialVolumes = volumes,
        initialRewatches = rewatches,
        initialText = text
    )
}

class UserRateState(
    private val context: UserRateEditorContext,
    private val enabledAutoCorrect: Boolean,
    initialStatus: UserRateStatus,
    initialScore: Int,
    initialEpisodes: Int,
    initialChapters: Int,
    initialVolumes: Int,
    initialRewatches: Int,
    initialText: String
) {
    val availableStatuses = buildList {
        add(Planned)
        add(Watching)
        add(Rewatching)
        if (context.entryStatus != EntryStatus.Ongoing) {
            add(Completed)
        }
        add(OnHold)
        add(Dropped)
    }

    private var _status = mutableStateOf(initialStatus)
    val status
        get() = _status.value

    val scoreState = ScoreState(initialScore)

    val episodesState = context.episodesLimit?.let {
        CounterState(initialEpisodes, it.range)
    }

    val chaptersState = context.chaptersLimit?.let {
        CounterState(initialChapters, it.range)
    }

    val volumesState = context.volumesLimit?.let {
        CounterState(initialVolumes, it.range)
    }

    val rewatchesState = CounterState(initialRewatches, 0..Int.MAX_VALUE)

    var text by mutableStateOf(initialText)

    val enabledSaveButton by derivedStateOf {
        !rewatchesState.isError &&
                episodesState.let { it == null || !it.isError } &&
                chaptersState.let { it == null || !it.isError } &&
                volumesState.let { it == null || !it.isError }
    }

    private val requireEpisodesLimit
        get() = requireNotNull(context.episodesLimit)

    private val requireChaptersLimit
        get() = requireNotNull(context.chaptersLimit)

    private val requireVolumesLimit
        get() = requireNotNull(context.volumesLimit)

    suspend fun collectEpisodesChanges() {
        if (!enabledAutoCorrect) return
        episodesState?.let {
            snapshotFlow { it.countInt }
                .filterNotNull()
                .collect { episodes ->
                    progressChanged(episodes, context.episodesLimit!!)
                }
        }
    }

    suspend fun collectChaptersChanges() {
        if (!enabledAutoCorrect) return
        chaptersState?.let {
            snapshotFlow { it.countInt }
                .filterNotNull()
                .collect { chapters ->
                    progressChanged(chapters, context.chaptersLimit!!)
                }
        }
    }

    suspend fun collectVolumesChanges() {
        if (!enabledAutoCorrect) return
        chaptersState?.let {
            snapshotFlow { it.countInt }
                .filterNotNull()
                .collect { volumes ->
                    progressChanged(volumes, context.volumesLimit!!)
                }
        }
    }

    suspend fun collectRewatchesChanges() {
        if (!enabledAutoCorrect) return
        snapshotFlow { rewatchesState.countInt }
            .filterNotNull()
            .collect { rewatches ->
                setStatus(Completed)
            }
    }

    fun setStatus(newStatus: UserRateStatus) {
        val oldStatus = _status.value
        if (newStatus == oldStatus) return

        _status.value = newStatus

        if (!enabledAutoCorrect) return

        when (newStatus) {
            Completed -> {
                episodesState?.let { setCountMax(it, requireEpisodesLimit) }
                chaptersState?.let { setCountMax(it, requireChaptersLimit) }
                volumesState?.let { setCountMax(it, requireVolumesLimit) }
                if (oldStatus == Rewatching) {
                    rewatchesState.countInt?.let {
                        rewatchesState.countStr = (it + 1).toString()
                    }
                }
            }

            Rewatching -> {
                episodesState?.countStr = "0"
                chaptersState?.countStr = "0"
                volumesState?.countStr = "0"
            }

            else -> {}
        }
    }

    private fun setCountMax(counterState: CounterState, limit: Limit) {
        if (limit is Limit.Unlimited) return
        counterState.countStr = limit.limit.toString()
    }

    private fun progressChanged(progress: Int, limit: Limit) {
        if (progress == limit.limit) {
            setStatus(Completed)
            return
        }
        if (status in listOf(Planned, OnHold, Dropped)) {
            setStatus(Watching)
        }
    }
}

val UserRateState.userRateValues: UserRateValues?
    get() {
        val episodes = episodesState?.run { countInt ?: return null }
        val chapters = chaptersState?.run { countInt ?: return null }
        val volumes = volumesState?.run { countInt ?: return null }
        val rewatches = rewatchesState.countInt ?: return null
        return UserRateValues(
            status = status,
            score = scoreState.targetScore,
            episodes = episodes,
            chapters = chapters,
            volumes = volumes,
            rewatches = rewatches,
            text = text
        )
    }

