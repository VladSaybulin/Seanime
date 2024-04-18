package ru.vladsaybulin.core.ui.userrate

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.EntryPoster
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.core.ui.ScoreStars
import ru.vladsaybulin.core.ui.SmallStarSize
import ru.vladsaybulin.core.ui.entry.EntryKindAndYearMetadata
import ru.vladsaybulin.core.ui.entry.UserRateStatusBadge
import ru.vladsaybulin.core.ui.strings.animeKindString
import ru.vladsaybulin.core.ui.strings.mangaKindString
import ru.vladsaybulin.model.common.EntryStatus.Ongoing
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.UserRateStatus.Rewatching
import ru.vladsaybulin.model.userrate.UserRateStatus.Watching
import ru.vladsaybulin.model.userrate.UserRateWithEntry
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.userrate.UserRateProgressLimit
import ru.vladsaybulin.model.userrate.UserRateProgressLimit.Companion.Unlimited
import ru.vladsaybulin.model.userrate.toLimit

@Composable
fun UserRateEntryCard(
    userRateWithEntry: UserRateWithEntry,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userRate = userRateWithEntry.userRate

    val name: String
    val poster: Image?
    val episodesLimit: UserRateProgressLimit?
    val chaptersLimit: UserRateProgressLimit?
    val volumesLimit: UserRateProgressLimit?
    val kindString: String?
    val airedInYear: Int?

    val inProgress = userRate.status == Watching || userRate.status == Rewatching

    if (userRateWithEntry.anime != null) {
        val anime = userRateWithEntry.anime!!
        name = anime.russianName ?: anime.name
        poster = anime.poster
        episodesLimit = when {
            !inProgress || anime.episodes == 1 -> null
            anime.episodes > 0 -> anime.episodes.toLimit()
            anime.status == Ongoing && anime.episodesAired > 0 -> anime.episodesAired.toLimit()
            else -> null
        }
        volumesLimit = null
        chaptersLimit = null
        kindString = animeKindString(anime.kind)
        airedInYear = anime.airedOn?.year
    } else if (userRateWithEntry.manga != null) {
        val manga = userRateWithEntry.manga!!
        name = manga.russianName ?: manga.name
        poster = manga.poster
        episodesLimit = null
        volumesLimit = if (inProgress && manga.chapters == 0 && manga.volumes > 0) {
            UserRateProgressLimit(manga.volumes)
        } else null
        chaptersLimit = if (inProgress && manga.chapters > 0) {
            UserRateProgressLimit(manga.chapters)
        } else Unlimited
        kindString = mangaKindString(manga.kind)
        airedInYear = manga.airedOn?.year
    } else throw IllegalArgumentException()

    Surface(
        onClick = onClick,
        modifier = modifier,
        border = BorderStroke(1.dp, ShikimoriTheme.colorScheme.outlineVariant),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Max),
        ) {
            Box(modifier = Modifier.clip(RoundedCornerShape(16.dp))) {
                EntryPoster(
                    poster = poster,
                    modifier = Modifier.width(128.dp)
                )
                if (userRate.status != UserRateStatus.None) {
                    UserRateStatusBadge(
                        userRateStatus = userRate.status,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                    )
                }
            }
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = name,
                    style = ShikimoriTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                EntryKindAndYearMetadata(
                    entryKindString = kindString,
                    airedInYear = airedInYear,
                    textStyle = ShikimoriTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.weight(1f))
                ScoreStars(
                    score = userRate.score.toFloat(),
                    starSize = SmallStarSize
                )
                Spacer(modifier = Modifier.height(8.dp))
                when {
                    episodesLimit != null -> Progress(
                        progress = userRate.episodes,
                        limit = episodesLimit,
                        unlimitedProgressStringRes = R.string.episodes_progress,
                        limitedProgressStringRes = R.string.episodes_progress_of_limit
                    )

                    volumesLimit != null -> Progress(
                        progress = userRate.volumes,
                        limit = volumesLimit,
                        unlimitedProgressStringRes = R.string.volumes_progress,
                        limitedProgressStringRes = R.string.volumes_progress_of_limit
                    )

                    chaptersLimit != null -> Progress(
                        progress = userRate.chapters,
                        limit = chaptersLimit,
                        unlimitedProgressStringRes = R.string.chapters_progress,
                        limitedProgressStringRes = R.string.chapters_progress_of_limit
                    )
                }
            }
        }
    }
}

@Composable
private fun Progress(
    progress: Int,
    limit: UserRateProgressLimit,
    unlimitedProgressStringRes: Int,
    limitedProgressStringRes: Int,
) {
    Column {
        if (limit != Unlimited) {
            LinearProgressIndicator(
                progress = { progress / limit.limit.toFloat() },
                modifier = Modifier.clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(2.dp))
        }
        Text(
            text = if (limit == Unlimited) {
                stringResource(id = unlimitedProgressStringRes, progress)
            } else {
                stringResource(id = limitedProgressStringRes, progress, limit.limit)
            },
            style = ShikimoriTheme.typography.bodySmall
        )
    }
}