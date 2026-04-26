package ru.vladsaybulin.core.ui.userrate

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.components.SeanimeUserRateLinearProgressIndicator
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.core.ui.entry.EntryInfoKindAndYear
import ru.vladsaybulin.core.ui.entry.list.EntryListItem
import ru.vladsaybulin.core.ui.score.ScoreStars
import ru.vladsaybulin.core.ui.score.SmallStarSize
import ru.vladsaybulin.core.ui2.strings.AnimeStrings
import ru.vladsaybulin.core.ui2.strings.MangaStrings
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.userrate.EditableUserRate
import ru.vladsaybulin.model.userrate.UserRateStatus.None
import ru.vladsaybulin.model.userrate.UserRateStatus.Rewatching
import ru.vladsaybulin.model.userrate.UserRateStatus.Watching
import ru.vladsaybulin.model.userrate.UserRateWithEntry

@Composable
fun UserRateEntryCard(
    userRateWithEntry: UserRateWithEntry,
    onAnimeClick: (Anime) -> Unit,
    onMangaClick: (Manga) -> Unit,
    onEditClick: (EditableUserRate) -> Unit,
    modifier: Modifier = Modifier,
    showUserRateBadge: Boolean = true
) {
    val userRate = userRateWithEntry.userRate

    val state = rememberUserrateEntryCardState(userRateWithEntry)

    EntryListItem(
        name = state.name,
        imageUrl = state.poster?.previewUrl,
        onClick = {
            if (userRateWithEntry.anime != null) {
                onAnimeClick(userRateWithEntry.anime!!)
            } else onMangaClick(userRateWithEntry.manga!!)
        },
        userRateStatus = if (showUserRateBadge) userRate.status else None,
        border = BorderStroke(1.dp, SeanimeTheme.colorScheme.outlineVariant),
        imageIgnoresPadding = true,
        containerShape = SeanimeTheme.shapes.large,
        imageShape = SeanimeTheme.shapes.large,
        containerColor = SeanimeTheme.colorScheme.surfaceColorAtElevation(1.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(Modifier.weight(1f)) {
                Spacer(modifier = Modifier.height(4.dp))
                EntryInfoKindAndYear(
                    kindText = state.kindStringId?.let { stringResource(it) },
                    year = state.airedInYear,
                )
                Spacer(modifier = Modifier.weight(1f))
                ScoreStars(
                    score = userRate.score.toFloat(),
                    starSize = SmallStarSize
                )
                Spacer(modifier = Modifier.height(8.dp))

                when {
                    state.episodesState != null -> ProgressIndicator(
                        state = state.episodesState,
                        unlimitedProgressStringRes = R.string.episodes_progress,
                        limitedProgressStringRes = R.string.episodes_progress_of_limit
                    )

                    state.volumesState != null -> ProgressIndicator(
                        state = state.volumesState,
                        unlimitedProgressStringRes = R.string.volumes_progress,
                        limitedProgressStringRes = R.string.volumes_progress_of_limit
                    )

                    state.chaptersState != null -> ProgressIndicator(
                        state = state.chaptersState,
                        unlimitedProgressStringRes = R.string.chapters_progress,
                        limitedProgressStringRes = R.string.chapters_progress_of_limit
                    )
                }
            }

            FilledTonalIconButton(
                onClick = { onEditClick(userRateWithEntry.asEditableUserRate()) },
                modifier = Modifier.align(Alignment.Bottom)
            ) {
                Icon(
                    imageVector = SeanimeIcons.Edit,
                    contentDescription = null,
                    tint = SeanimeTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun ProgressIndicator(
    state: UserRateProgressState,
    unlimitedProgressStringRes: Int,
    limitedProgressStringRes: Int,
) {
    Column {

        if (state.isProgressIndicatorVisible()) {
            SeanimeUserRateLinearProgressIndicator(
                progress = state.progressFraction,
                availableProgress = state.availableFraction
            )
            Spacer(modifier = Modifier.height(2.dp))
        }

        Text(
            text = if (state.max == 0) {
                stringResource(id = unlimitedProgressStringRes, state.progress)
            } else {
                stringResource(id = limitedProgressStringRes, state.progress, state.max)
            },
            style = SeanimeTheme.typography.bodySmall
        )
    }
}

private class UserRateProgressState(
    val progress: Int,
    val available: Int,
    val total: Int
) {
    val max = when {
        total != 0 -> total
        available != 0 -> available
        progress != 0 -> progress
        else -> 0
    }

    val progressFraction: Float
        get() {
            require(isProgressIndicatorVisible())
            return progress / max.toFloat()
        }

    val availableFraction: Float
        get() {
            require(isProgressIndicatorVisible())
            return available / max.toFloat()
        }

    fun isProgressIndicatorVisible() = max != 0
}

@Composable
private fun rememberUserrateEntryCardState(
    userRateWithEntry: UserRateWithEntry
): UserRateEntryCardState = remember(userRateWithEntry) {
    when {
        userRateWithEntry.anime != null -> createAnimeUserRateEntryCardState(userRateWithEntry)
        userRateWithEntry.manga != null -> createMangaUserRateEntryCardState(userRateWithEntry)
        else -> throw IllegalArgumentException()
    }
}

private fun createAnimeUserRateEntryCardState(
    userRateWithEntry: UserRateWithEntry
): UserRateEntryCardState {
    val anime = userRateWithEntry.anime!!
    val inProgress = userRateWithEntry.userRate.status in listOf(Watching, Rewatching)

    return UserRateEntryCardState(
        name = anime.russianName ?: anime.name,
        poster = anime.poster,
        episodesState = if (inProgress) {
            UserRateProgressState(
                progress = userRateWithEntry.userRate.episodes,
                available = anime.episodesAired,
                total = anime.episodes
            )
        } else null,
        volumesState = null,
        chaptersState = null,
        kindStringId = AnimeStrings.kindId(anime.kind),
        airedInYear = anime.airedOn?.year
    )
}

private fun createMangaUserRateEntryCardState(
    userRateWithEntry: UserRateWithEntry
): UserRateEntryCardState {
    val manga = userRateWithEntry.manga!!
    val inProgress = userRateWithEntry.userRate.status in listOf(Watching, Rewatching)

    val volumesState = if (inProgress && manga.chapters == 0 && manga.volumes > 0) {
        UserRateProgressState(
            progress = userRateWithEntry.userRate.volumes,
            available = manga.volumes,
            total = manga.volumes
        )
    } else null

    return UserRateEntryCardState(
        name = manga.russianName ?: manga.name,
        poster = manga.poster,
        episodesState = null,
        volumesState = volumesState,
        chaptersState = if (volumesState == null) {
            UserRateProgressState(
                progress = userRateWithEntry.userRate.chapters,
                available = manga.chapters,
                total = manga.chapters
            )
        } else null,
        kindStringId = MangaStrings.kindId(manga.kind),
        airedInYear = manga.airedOn?.year
    )
}

private data class UserRateEntryCardState(
    val name: String,
    val poster: Image?,
    val episodesState: UserRateProgressState?,
    val chaptersState: UserRateProgressState?,
    val volumesState: UserRateProgressState?,
    val kindStringId: Int?,
    val airedInYear: Int?
)

private fun UserRateWithEntry.asEditableUserRate() = EditableUserRate(
    userRate = userRate,
    titleType = if (anime != null) EntryType.Anime else EntryType.Manga,
    entryStatus = anime?.status ?: manga?.status ?: EntryStatus.None,
    maxEpisodes = anime?.let {
        when (it.status) {
            EntryStatus.Ongoing -> it.episodesAired
            else -> it.episodes
        }
    } ?: -1,
    maxChapters = manga?.chapters ?: -1,
    maxVolumes = manga?.volumes ?: -1,
)