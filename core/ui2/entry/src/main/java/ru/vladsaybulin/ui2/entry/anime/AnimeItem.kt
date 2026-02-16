package ru.vladsaybulin.ui2.entry.anime

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui2.strings.AnimeStrings
import ru.vladsaybulin.core.ui2.strings.compose.ProvideTitleStringsByType
import ru.vladsaybulin.core.ui2.strings.compose.asString
import ru.vladsaybulin.core.ui2.strings.compose.asStringOrNull
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.ui2.entry.EntryGridItem
import ru.vladsaybulin.ui2.entry.EntryItemDefaults
import ru.vladsaybulin.ui2.entry.EntryItemStyle
import ru.vladsaybulin.ui2.entry.EntryListItem
import ru.vladsaybulin.ui2.entry.TitleGridItemAdditionalContent
import ru.vladsaybulin.ui2.entry.TitleListItemDefaultAdditionalContent
import ru.vladsaybulin.ui2.entry.preview.AnimeItemPreviewParameterProvider

@Composable
fun AnimeGridItem(
    anime: Anime,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    style: EntryItemStyle = EntryItemDefaults.regularGridStyle(),
    additionalContent: (@Composable () -> Unit)? = { AnimeGridItemDefaultAdditionalContent(anime) },
) {
    EntryGridItem(
        name = anime.name,
        russianName = anime.russianName,
        poster = anime.poster,
        onClick = onClick,
        modifier = modifier,
        userRateStatus = userRateStatus,
        style = style,
        additionalContent = additionalContent
    )
}

@Composable
fun AnimeListItem(
    anime: Anime,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    posterWidth: Dp = EntryItemDefaults.listItemPosterWidth,
    style: EntryItemStyle = EntryItemDefaults.regularListStyle(),
    additionalContent: (@Composable () -> Unit)? = { AnimeListItemDefaultAdditionalContent(anime) },
) {
    EntryListItem(
        name = anime.name,
        russianName = anime.russianName,
        poster = anime.poster,
        onClick = onClick,
        modifier = modifier,
        userRateStatus = userRateStatus,
        posterWidth = posterWidth,
        style = style,
        additionalContent = additionalContent
    )
}

fun LazyListScope.animeItems(
    animes: List<Anime>,
    onItemClick: (Anime) -> Unit,
    itemModifier: Modifier = Modifier,
    userRateStatus: (Anime) -> UserRateStatus,
    style: EntryItemStyle? = null,
    additionalContent: (@Composable (Anime) -> Unit)? = { AnimeGridItemDefaultAdditionalContent(it) },
) {

}

@Composable
fun AnimeGridItemDefaultAdditionalContent(anime: Anime) {
    TitleGridItemAdditionalContent(
        kindStr = anime.kind.asStringOrNull(),
        year = anime.airedOn?.year
    )
}

@Composable
fun AnimeListItemDefaultAdditionalContent(anime: Anime) {
    TitleListItemDefaultAdditionalContent(
        status = anime.status,
        year = anime.airedOn?.year ?: anime.releasedOn?.year,
        kindAndVolumes = kindAndEpisodesFormatted(
            kind = anime.kind,
            episodes = anime.episodes,
            episodesAired = anime.episodesAired,
            status = anime.status
        ),
        score = anime.score
    )
}

@Composable
@ReadOnlyComposable
private fun kindAndEpisodesFormatted(
    kind: AnimeKind,
    episodes: Int,
    episodesAired: Int,
    status: EntryStatus
): String? {
    val kindStr = kind.asStringOrNull()
    val episodesStr =
        AnimeStrings.getProgressFormat(
            aired = episodesAired,
            total = episodes,
            isOngoing = status == EntryStatus.Ongoing,
            isMovie = kind == AnimeKind.Movie
        )?.asString()

    val layoutDirection = LocalLayoutDirection.current

    val finalStr = listOfNotNull(kindStr, episodesStr).apply {
        if (layoutDirection == LayoutDirection.Rtl) {
            reversed()
        }
    }.joinToString(separator = " • ")

    return finalStr.takeIf { it.isNotEmpty() }
}

@Preview
@Composable
private fun AnimeListItemPreview(
    @PreviewParameter(AnimeItemPreviewParameterProvider::class) anime: Anime
) {
    SeanimeTheme {
        ProvideTitleStringsByType(EntryType.Anime) {
            AnimeListItem(
                anime = anime,
                onClick = {}
            )
        }
    }
}

@Composable
@Preview
fun AnimeGridItemPreview(@PreviewParameter(AnimeItemPreviewParameterProvider::class) anime: Anime) {
    SeanimeTheme {
        ProvideTitleStringsByType(EntryType.Anime) {
            AnimeGridItem(
                modifier = Modifier.width(150.dp),
                anime = anime,
                onClick = {}
            )
        }
    }
}
