package ru.vladsaybulin.core.ui.userrate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.ui.ScoreStars
import ru.vladsaybulin.core.ui.entry.EntryKindAndYearMetadata
import ru.vladsaybulin.core.ui.strings.animeKindString
import ru.vladsaybulin.core.ui.strings.mangaKindString
import ru.vladsaybulin.model.Anime
import ru.vladsaybulin.model.EntryStatus.Ongoing
import ru.vladsaybulin.model.Manga
import ru.vladsaybulin.model.UserRate
import ru.vladsaybulin.model.userrate.UserRateProgressLimit
import ru.vladsaybulin.model.userrate.UserRateProgressLimit.Companion.Unlimited
import ru.vladsaybulin.model.userrate.UserRateProgressLimit.Companion.Unspecified
import ru.vladsaybulin.model.userrate.toLimit

@Composable
fun AnimeUserRateData(
    anime: Anime,
    userRate: UserRate,
    modifier: Modifier = Modifier,
) {
    UserRateData(
        userRate = userRate,
        entryKindString = animeKindString(anime.kind),
        airedInYear = anime.airedOn?.year,
        episodesLimit = when {
            anime.status == Ongoing && anime.episodesAired > 0 -> anime.episodesAired.toLimit()
            anime.episodes > 0 -> anime.episodes.toLimit()
            else -> Unlimited
        },
        chaptersLimit = Unspecified,
        volumesLimit = Unspecified,
        modifier = modifier
    )
}

@Composable
fun MangaUserRateData(
    manga: Manga,
    userRate: UserRate,
    modifier: Modifier = Modifier
) {
    val volumesLimit = if (manga.volumes > 0 && manga.chapters == 0) {
        manga.volumes.toLimit()
    } else Unspecified

    val chaptersLimit = if (volumesLimit == Unspecified && manga.chapters > 0) {
        manga.chapters.toLimit()
    } else Unlimited

    UserRateData(
        userRate = userRate,
        entryKindString = mangaKindString(manga.kind),
        airedInYear = manga.airedOn?.year,
        episodesLimit = Unspecified,
        chaptersLimit = chaptersLimit,
        volumesLimit = volumesLimit,
        modifier = modifier
    )
}

@Composable
private fun UserRateData(
    userRate: UserRate,
    entryKindString: String?,
    airedInYear: Int?,
    modifier: Modifier = Modifier,
    episodesLimit: UserRateProgressLimit,
    chaptersLimit: UserRateProgressLimit,
    volumesLimit: UserRateProgressLimit,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Column {
            EntryKindAndYearMetadata(
                entryKindString = entryKindString,
                airedInYear = airedInYear
            )

            Spacer(modifier = Modifier.weight(1f))

            ScoreStars(
                score = userRate.score.toFloat(),
                starSize = DpSize(24.dp, 24.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))


        }
    }
}
