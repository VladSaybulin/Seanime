package ru.vladsaybulin.core.ui2.entry.userrate

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui2.entry.EntryListItem
import ru.vladsaybulin.core.ui2.entry.R
import ru.vladsaybulin.core.ui2.entry.additional.AdditionalContentKindAndYear
import ru.vladsaybulin.core.ui2.entry.preview.ListOfAnimesPreviewParameterProvider
import ru.vladsaybulin.core.ui2.strings.compose.ProvideTitleStringsByType
import ru.vladsaybulin.core.ui2.strings.compose.asStringOrNull
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.userrate.RatedTitle
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.UserRateWithEntry
import kotlin.math.abs
import kotlin.math.max

@Composable
fun UserRateItem(
    userRateWithEntry: UserRateWithEntry,
    onAnimeClick: (Anime) -> Unit,
    onMangaClick: (Manga) -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    UserRateItem(
        ratedTitle = remember(userRateWithEntry) {
            userRateWithEntry.anime?.let {
                RatedTitle.Anime(
                    anime = it,
                    userRate = userRateWithEntry.userRate
                )
            } ?: run {
                RatedTitle.Manga(
                    manga = userRateWithEntry.manga!!,
                    userRate = userRateWithEntry.userRate
                )
            }
        },
        onAnimeClick = onAnimeClick,
        onMangaClick = onMangaClick,
        onEditClick = onEditClick,
        modifier = modifier
    )
}

@Composable
fun UserRateItem(
    ratedTitle: RatedTitle,
    onAnimeClick: (Anime) -> Unit,
    onMangaClick: (Manga) -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (ratedTitle) {
        is RatedTitle.Anime -> ratedTitle.anime.let { anime ->
            UserRateItem(
                modifier = modifier,
                type = EntryType.Anime,
                name = anime.name,
                russianName = anime.russianName,
                poster = anime.poster,
                kind = anime.kind.asStringOrNull(),
                airedOnYear = anime.airedOn?.year ?: anime.releasedOn?.year,
                status = ratedTitle.userRate.status,
                score = ratedTitle.userRate.score,
                progression = rememberAnimeProgression(ratedTitle),
                onTitleClick = { onAnimeClick(anime) },
                onEditClick = onEditClick
            )
        }

        is RatedTitle.Manga -> ratedTitle.manga.let { manga ->
            UserRateItem(
                modifier = modifier,
                type = EntryType.Manga,
                name = manga.name,
                russianName = manga.russianName,
                poster = manga.poster,
                kind = manga.kind.asStringOrNull(),
                airedOnYear = manga.airedOn?.year ?: manga.releasedOn?.year,
                status = ratedTitle.userRate.status,
                score = ratedTitle.userRate.score,
                progression = rememberMangaProgression(ratedTitle),
                onTitleClick = { onMangaClick(manga) },
                onEditClick = onEditClick
            )
        }
    }
}

private data class Progression(
    val field: Field,
    val completed: Int,
    val available: Int,
    val total: Int
) {
    enum class Field {
        Episodes, Chapters, Volumes
    }
}

@Composable
private fun rememberAnimeProgression(ratedTitle: RatedTitle.Anime): Progression? {
    return when (ratedTitle.userRate.status) {
        UserRateStatus.Planned -> null

        UserRateStatus.Completed -> Progression(
            field = Progression.Field.Episodes,
            completed = ratedTitle.userRate.episodes,
            available = 0,
            total = 0
        )

        else -> {
            val anime = ratedTitle.anime
            val total = max(anime.episodes, anime.episodesAired)
            if (total > 1) {
                Progression(
                    field = Progression.Field.Episodes,
                    completed = ratedTitle.userRate.episodes,
                    available = if (anime.status == EntryStatus.Released) total else anime.episodesAired,
                    total = total
                )
            } else null
        }
    }
}

@Composable
private fun rememberMangaProgression(ratedTitle: RatedTitle.Manga): Progression? {

    if (ratedTitle.userRate.status == UserRateStatus.Planned) return null

    return when {
        ratedTitle.userRate.chapters > 0 -> Progression(
            field = Progression.Field.Chapters,
            completed = ratedTitle.userRate.chapters,
            available = 0,
            total = ratedTitle.manga.chapters
        )

        ratedTitle.userRate.volumes > 0 -> Progression(
            field = Progression.Field.Volumes,
            completed = ratedTitle.userRate.volumes,
            available = 0,
            total = ratedTitle.manga.volumes
        )

        ratedTitle.manga.chapters == 0 && ratedTitle.manga.volumes > 0 -> Progression(
            field = Progression.Field.Volumes,
            completed = 0,
            available = 0,
            total = ratedTitle.manga.volumes
        )

        else -> null
    }
}

@Composable
private fun UserRateItem(
    modifier: Modifier,
    type: EntryType,
    name: String,
    russianName: String?,
    poster: Image?,
    kind: String?,
    airedOnYear: Int?,
    status: UserRateStatus,
    score: Int,
    progression: Progression?,
    onTitleClick: () -> Unit,
    onEditClick: () -> Unit,
) {
    ProvideTitleStringsByType(type) {
        EntryListItem(
            name = name,
            russianName = russianName,
            poster = poster,
            onClick = onTitleClick,
            modifier = modifier,
            userRateStatus = status
        ) {
            Row{
                Column(modifier = Modifier.weight(1f)) {
                    ProvideTextStyle(SeanimeTheme.typography.labelSmall) {
                        AdditionalContentKindAndYear(
                            kindStr = kind,
                            year = airedOnYear
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        //TODO display score stars after implementation

                        progression?.let { UserProgression(it) }
                    }
                }

                Spacer(Modifier.width(4.dp))

                FilledTonalIconButton(
                    onClick = { onEditClick() },
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
}

@Composable
private fun UserProgression(progression: Progression) {
    val hasLimit = progression.total > 0 || progression.available > 0

    if (hasLimit) {
        ProgressIndicator(progression)
        LimitedProgressionDescription(progression)
    } else {
        UnlimitedProgressionDescription(progression)
    }
}

@Composable
private fun ProgressIndicator(
    progression: Progression
) {
    check(progression.total > 0f) { "Don't call ProgressIndicator when total and available is are 0" }

    val total = progression.total.toFloat()
    val completedFraction = progression.completed / total
    val availableFraction = progression.available / total

    val colors = SeanimeTheme.colorScheme
    val completedColor = colors.primary
    val availableColor = colors.primary.copy(alpha = AVAILABLE_COLOR_OPACITY)
    val color = colors.surfaceVariant

    Canvas(
        modifier = Modifier
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = progression.completed.toFloat(),
                    range = 0f..total
                )
            }
            .fillMaxWidth()
            .height(LinearIndicatorHeight)
    ) {
        drawLinearIndicator(
            startFraction = 0f,
            endFraction = 1f,
            color = color,
            strokeWidth = size.height,
            strokeCap = StrokeCap.Round
        )

        drawLinearIndicator(
            startFraction = 0f,
            endFraction = availableFraction,
            color = availableColor,
            strokeWidth = size.height,
            strokeCap = StrokeCap.Round
        )

        drawLinearIndicator(
            startFraction = 0f,
            endFraction = completedFraction,
            color = completedColor,
            strokeWidth = size.height,
            strokeCap = StrokeCap.Round
        )
    }
}

// Copied from androidx.compose.material3.LinearProgressIndicator
private fun DrawScope.drawLinearIndicator(
    startFraction: Float,
    endFraction: Float,
    color: Color,
    strokeWidth: Float,
    strokeCap: StrokeCap,
) {
    val width = size.width
    val height = size.height
    // Start drawing from the vertical center of the stroke
    val yOffset = height / 2

    val isLtr = layoutDirection == LayoutDirection.Ltr
    val barStart = (if (isLtr) startFraction else 1f - endFraction) * width
    val barEnd = (if (isLtr) endFraction else 1f - startFraction) * width

    // if there isn't enough space to draw the stroke caps, fall back to StrokeCap.Butt
    if (strokeCap == StrokeCap.Butt || height > width) {
        // Progress line
        drawLine(color, Offset(barStart, yOffset), Offset(barEnd, yOffset), strokeWidth)
    } else {
        // need to adjust barStart and barEnd for the stroke caps
        val strokeCapOffset = strokeWidth / 2
        val coerceRange = strokeCapOffset..(width - strokeCapOffset)
        val adjustedBarStart = barStart.coerceIn(coerceRange)
        val adjustedBarEnd = barEnd.coerceIn(coerceRange)

        if (abs(endFraction - startFraction) > 0) {
            // Progress line
            drawLine(
                color,
                Offset(adjustedBarStart, yOffset),
                Offset(adjustedBarEnd, yOffset),
                strokeWidth,
                strokeCap,
            )
        }
    }
}

@Composable
private fun LimitedProgressionDescription(progression: Progression) {
    Text(
        text = when (progression.field) {
            Progression.Field.Episodes -> R.string.core_ui2_entry_user_rate_item_limited_episodes
            Progression.Field.Chapters -> R.string.core_ui2_entry_user_rate_item_limited_chapters
            Progression.Field.Volumes -> R.string.core_ui2_entry_user_rate_item_limited_volumes
        }.let { stringResource(it, progression.completed, max(progression.total, progression.available)) }
    )
}

@Composable
private fun UnlimitedProgressionDescription(progression: Progression) {
    Text(
        text = when (progression.field) {
            Progression.Field.Episodes -> R.string.core_ui2_entry_user_rate_item_unlimited_episodes
            Progression.Field.Chapters -> R.string.core_ui2_entry_user_rate_item_unlimited_chapters
            Progression.Field.Volumes -> R.string.core_ui2_entry_user_rate_item_unlimited_volumes
        }.let { stringResource(it, progression.completed) },
    )
}

@Composable
@Preview
fun UserRateDataPreview(
    @PreviewParameter(ListOfAnimesPreviewParameterProvider::class) anime: List<Anime>
) {
    SeanimeTheme {
        UserRateItem(
            ratedTitle = RatedTitle.Anime(
                anime = anime[2],
                userRate = anime[2].userRate!!
            ),
            onAnimeClick = {},
            onMangaClick = {},
            onEditClick = {}
        )
    }
}

private val LinearIndicatorHeight = 6.dp
private const val AVAILABLE_COLOR_OPACITY = 0.3f