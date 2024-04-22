package ru.vladsaybulin.core.ui.anime

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vladsaybulin.core.ui.entry.EntryInfoScore
import ru.vladsaybulin.core.ui.entry.EntryInfoStatusAndDatesText
import ru.vladsaybulin.core.ui.entry.EntryListItem
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.AnimeWithUserRate
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.userrate.UserRateStatus

@Composable
fun AnimeListItem(
    anime: Anime,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    metadata: (@Composable ColumnScope.() -> Unit)? = { AnimeGridMetadata(anime) },
) {
    EntryListItem(
        name = anime.russianName ?: anime.name,
        userRateStatus = userRateStatus,
        imageUrl = anime.poster?.previewUrl,
        onClick = onClick,
        modifier = modifier,
        metadata = metadata
    )
}

@Composable
fun AnimeListItem(
    animeWithUserRate: AnimeWithUserRate,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    metadata: (@Composable ColumnScope.() -> Unit)? = { AnimeGridMetadata(animeWithUserRate.anime) },
) {
    val anime = animeWithUserRate.anime

    EntryListItem(
        name = anime.russianName ?: anime.name,
        userRateStatus = animeWithUserRate.userRate?.status ?: UserRateStatus.None,
        imageUrl = anime.poster?.previewUrl,
        onClick = onClick,
        modifier = modifier,
        metadata = metadata
    )
}

@Composable
fun ColumnScope.DefaultAnimeListItemData(anime: Anime) {
    AnimeInfoKindAndEpisodesAndDurationText(
        kind = anime.kind,
        episodes = anime.episodes,
        episodesAired = anime.episodesAired,
        duration = 0,
        isOngoing = anime.status == EntryStatus.Ongoing
    )

    EntryInfoStatusAndDatesText(
        entryStatus = anime.status,
        airedOn = anime.airedOn,
        releasedOn = anime.releasedOn
    )

    EntryInfoScore(score = anime.score ?: 0f)
}
