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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.components.SeanimeUserRateLinearProgressIndicator
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.core.ui.entry.EntryInfoKindAndYear
import ru.vladsaybulin.core.ui.entry.EntryListItem
import ru.vladsaybulin.core.ui.score.ScoreStars
import ru.vladsaybulin.core.ui.score.SmallStarSize
import ru.vladsaybulin.core.ui.strings.animeKindString
import ru.vladsaybulin.core.ui.strings.mangaKindString
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.userrate.UserRateStatus.None
import ru.vladsaybulin.model.userrate.UserRateStatus.Rewatching
import ru.vladsaybulin.model.userrate.UserRateStatus.Watching
import ru.vladsaybulin.model.userrate.UserRateWithEntry

@Composable
fun UserRateEntryCard(
    userRateWithEntry: UserRateWithEntry,
    onAnimeClick: (Anime) -> Unit,
    onMangaClick: (Manga) -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
    showUserRateBadge: Boolean = true
) {
    val userRate = userRateWithEntry.userRate

    val name: String
    val poster: Image?
    val episodesState: UserRateProgressState?
    val chaptersState: UserRateProgressState?
    val volumesState: UserRateProgressState?
    val kindString: String?
    val airedInYear: Int?

    val inProgress = userRate.status == Watching || userRate.status == Rewatching

    if (userRateWithEntry.anime != null) {
        val anime = userRateWithEntry.anime!!

        name = anime.russianName ?: anime.name
        poster = anime.poster
        episodesState = UserRateProgressState(
            progress = userRate.episodes,
            available = anime.episodesAired,
            max = anime.episodes
        )
        volumesState = null
        chaptersState = null
        kindString = animeKindString(anime.kind)
        airedInYear = anime.airedOn?.year
    } else if (userRateWithEntry.manga != null) {
        val manga = userRateWithEntry.manga!!

        name = manga.russianName ?: manga.name
        poster = manga.poster
        episodesState = null
        volumesState = if (inProgress && manga.chapters == 0 && manga.volumes > 0) {
            UserRateProgressState(
                progress = userRate.volumes,
                available = manga.volumes,
                max = manga.volumes
            )
        } else null
        chaptersState = if (volumesState == null) {
            UserRateProgressState(
                progress = userRate.chapters,
                available = manga.chapters,
                max = manga.chapters
            )
        } else null
        kindString = mangaKindString(manga.kind)
        airedInYear = manga.airedOn?.year
    } else throw IllegalArgumentException()

    EntryListItem(
        name = name,
        imageUrl = poster?.previewUrl,
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
                    kindText = kindString,
                    year = airedInYear,
                )
                Spacer(modifier = Modifier.weight(1f))
                ScoreStars(
                    score = userRate.score.toFloat(),
                    starSize = SmallStarSize
                )
                Spacer(modifier = Modifier.height(8.dp))

                when {
                    episodesState != null -> ProgressIndicator(
                        state = episodesState,
                        unlimitedProgressStringRes = R.string.episodes_progress,
                        limitedProgressStringRes = R.string.episodes_progress_of_limit
                    )

                    volumesState != null -> ProgressIndicator(
                        state = volumesState,
                        unlimitedProgressStringRes = R.string.volumes_progress,
                        limitedProgressStringRes = R.string.volumes_progress_of_limit
                    )

                    chaptersState != null -> ProgressIndicator(
                        state = chaptersState,
                        unlimitedProgressStringRes = R.string.chapters_progress,
                        limitedProgressStringRes = R.string.chapters_progress_of_limit
                    )
                }
            }

            FilledTonalIconButton(
                onClick = onEditClick,
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
    max: Int
) {
    val max = when {
        max != 0 -> max
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