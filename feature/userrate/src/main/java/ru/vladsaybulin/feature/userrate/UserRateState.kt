package ru.vladsaybulin.feature.userrate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.UserRateStatus.Completed
import ru.vladsaybulin.model.userrate.UserRateStatus.Dropped
import ru.vladsaybulin.model.userrate.UserRateStatus.OnHold
import ru.vladsaybulin.model.userrate.UserRateStatus.Planned
import ru.vladsaybulin.model.userrate.UserRateStatus.Rewatching
import ru.vladsaybulin.model.userrate.UserRateStatus.Watching
import ru.vladsaybulin.model.userrate.UserRateValues
import ru.vladsaybulin.model.userrate.UserRateWithEntry

@Composable
fun rememberUserRateState(
    userRateWithEntry: UserRateWithEntry,
    autoCorrectUserRate: Boolean
): UserRateState = with(userRateWithEntry.userRate) {
    val entryStatus = userRateWithEntry.anime?.status ?: userRateWithEntry.manga!!.status

    var endAutocorrectUserRate = autoCorrectUserRate
    var availableStatuses = getAvailableUserRateStatuses(autoCorrectUserRate, entryStatus)
    if (autoCorrectUserRate && !availableStatuses.contains(userRateWithEntry.userRate.status)) {
        availableStatuses = getAvailableUserRateStatuses(false)
        endAutocorrectUserRate = false
    }

    return remember(userRateWithEntry, endAutocorrectUserRate) {
        UserRateState(
            availableStatuses = availableStatuses,
            initialScore = score,
            initialStatus = status,
            episodesLimit = userRateWithEntry.anime?.episodesLimit(endAutocorrectUserRate),
            initialEpisodes = episodes,
            chaptersLimit = userRateWithEntry.manga?.chaptersLimit(),
            initialChapters = chapters,
            volumesLimit = userRateWithEntry.manga?.volumesLimit(),
            initialVolumes = volumes,
            initialRewatches = rewatches,
            initialText = text,
            isOngoing = entryStatus == EntryStatus.Ongoing,
            enabledAutoCorrect = endAutocorrectUserRate
        )
    }
}

@Composable
fun rememberUserRateState(
    entryType: EntryType,
    entryStatus: EntryStatus,
    initialUserRate: UserRateValues,
    availableUserRateStatuses: List<UserRateStatus>,
    episodesLimit: Int,
    chaptersLimit: Int,
    volumesLimit: Int
): UserRateState = remember(
    entryType,
    initialUserRate,
    availableUserRateStatuses,
    episodesLimit,
    chaptersLimit,
    volumesLimit
) {
    UserRateState(
        availableStatuses = availableUserRateStatuses.toImmutableList(),
        initialScore = initialUserRate.score ?: 0,
        initialStatus = initialUserRate.status,
        episodesLimit = episodesLimit.takeIf { it > 0 }?.let(::CounterLimit),
        initialEpisodes = initialUserRate.episodes ?: 0,
        chaptersLimit = chaptersLimit.takeIf { it > 0 }?.let(::CounterLimit),
        initialChapters = initialUserRate.chapters ?: 0,
        volumesLimit = volumesLimit.takeIf { it > 0 }?.let(::CounterLimit),
        initialVolumes = initialUserRate.volumes ?: 0,
        initialRewatches = initialUserRate.rewatches ?: 0,
        initialText = initialUserRate.text ?: "",
        isOngoing = entryStatus == EntryStatus.Ongoing,
        enabledAutoCorrect = true
    )
}

fun Anime.episodesLimit(autoCorrect: Boolean) = when {
    episodes == 1 -> null
    autoCorrect && status == EntryStatus.Anons -> null
    autoCorrect && status == EntryStatus.Ongoing -> CounterLimit(episodesAired)
    episodes == 0 -> CounterLimit.Unlimited
    else -> CounterLimit(episodes)
}

fun Manga.chaptersLimit() = when {
    chapters == 0 -> CounterLimit.Unlimited
    chapters == 1 -> null
    else -> CounterLimit(chapters)
}

fun Manga.volumesLimit() = when {
    volumes == 0 -> CounterLimit.Unlimited
    volumes == 1 -> null
    else -> CounterLimit(volumes)
}

@Stable
class UserRateState(
    val availableStatuses: ImmutableList<UserRateStatus>,
    initialStatus: UserRateStatus,
    initialScore: Int,
    episodesLimit: CounterLimit?,
    initialEpisodes: Int,
    chaptersLimit: CounterLimit?,
    initialChapters: Int,
    volumesLimit: CounterLimit?,
    initialVolumes: Int,
    initialRewatches: Int,
    initialText: String,
    private val isOngoing: Boolean,
    private val enabledAutoCorrect: Boolean,
) {
    private var _status = mutableStateOf(initialStatus)
    var status: UserRateStatus
        get() = _status.value
        set(value) {
            _status.value = value
            onStatusChanged(_status.value, value)
        }


    var score by mutableIntStateOf(initialScore)

    val episodesCounterState = if (episodesLimit != null) {
        CounterState(
            initialCount = initialEpisodes,
            limit = episodesLimit,
            onChanged = { onProgressCountChanged(it, episodesLimit) }
        )
    } else null

    val chaptersCounterState = if (chaptersLimit != null) {
        CounterState(
            initialCount = initialChapters,
            limit = chaptersLimit,
            onChanged = {
                onProgressCountChanged(it, chaptersLimit)
            }
        )
    } else null

    val volumesCounterState = if (volumesLimit != null) {
        CounterState(
            initialCount = initialVolumes,
            limit = volumesLimit,
            onChanged = {
                onProgressCountChanged(it, volumesLimit)
            }
        )
    } else null

    val rewatchesCounterState = CounterState(
        initialCount = initialRewatches,
        limit = CounterLimit.Unlimited,
        onChanged = ::onRewatchesCountChanged
    )

    var text by mutableStateOf(initialText)

    val enabledSaveButton by derivedStateOf {
        !(episodesCounterState?.isError ?: false) &&
                !(chaptersCounterState?.isError ?: false) &&
                !(volumesCounterState?.isError ?: false)
    }

    private val inProgress: Boolean
        get() = status.let { it == Watching || it == Rewatching }

    val progressEnabled: Boolean
        get() = !enabledAutoCorrect || inProgress

    private fun onStatusChanged(oldStatus: UserRateStatus, newStatus: UserRateStatus) {
        if (!enabledAutoCorrect) return

        when (newStatus) {
            Completed -> {
                setMaxProgressCounters()
                if (oldStatus == Rewatching) {
                    rewatchesCounterState.onIncrement()
                }
            }

            Planned, Rewatching -> resetProgressCounters()
            else -> Unit
        }
    }

    private fun onProgressCountChanged(count: Int, limit: CounterLimit) {
        if (!enabledAutoCorrect || !inProgress) return
        if (!isOngoing && isLimitReached(count, limit)) {
            _status.value = Completed
            setMaxProgressCounters()
            if (status == Rewatching) {
                rewatchesCounterState.onIncrement()
            }
        }
    }

    private fun onRewatchesCountChanged(rewatches: Int) {
        if (!enabledAutoCorrect) return
        setMaxProgressCounters()
        _status.value = Completed
    }

    private fun isLimitReached(count: Int, limit: CounterLimit) =
        limit != CounterLimit.Unlimited && count == limit.value

    private fun setMaxProgressCounters() {
        episodesCounterState?.setMax()
        chaptersCounterState?.setMax()
        volumesCounterState?.setMax()
    }

    private fun resetProgressCounters() {
        episodesCounterState?.setZero()
        chaptersCounterState?.setZero()
        volumesCounterState?.setZero()
    }
}

fun getAvailableUserRateStatuses(
    autocorrectUserRate: Boolean,
    entryStatus: EntryStatus = EntryStatus.None
) = buildList {
    add(Planned)
    if (!autocorrectUserRate || entryStatus != EntryStatus.Anons) {
        add(Watching)
        add(Rewatching)
    }
    if (!autocorrectUserRate || entryStatus == EntryStatus.Released) {
        add(Completed)
    }
    add(OnHold)
    add(Dropped)
}.toImmutableList()

private fun CounterState.setZero() {
    count = 0
}

private fun CounterState.setMax() {
    if (limit == CounterLimit.Unlimited) return
    count = limit.value
}

val UserRateState.currentUserRateValues: UserRateValues
    get() = UserRateValues(
        status = status,
        score = score,
        episodes = episodesCounterState?.count,
        chapters = chaptersCounterState?.count,
        volumes = volumesCounterState?.count,
        rewatches = rewatchesCounterState.count,
        text = text
    )

