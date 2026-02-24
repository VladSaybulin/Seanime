package ru.vladsaybulin.ui2.entry.anime

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
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
import ru.vladsaybulin.ui2.entry.EntryCarouselItem
import ru.vladsaybulin.ui2.entry.EntryGridItem
import ru.vladsaybulin.ui2.entry.EntryItemColors
import ru.vladsaybulin.ui2.entry.EntryItemDefaults
import ru.vladsaybulin.ui2.entry.EntryListItem
import ru.vladsaybulin.ui2.entry.additional.TitleGridItemAdditionalContent
import ru.vladsaybulin.ui2.entry.additional.TitleListItemDefaultAdditionalContent
import ru.vladsaybulin.ui2.entry.preview.AnimeItemPreviewParameterProvider
import ru.vladsaybulin.ui2.entry.preview.ListOfAnimesPreviewParameterProvider

@Composable
@NonRestartableComposable
fun AnimeGridItem(
    anime: Anime,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    colors: EntryItemColors = EntryItemDefaults.basedOnUserRateStatusColors(userRateStatus),
    nameStyle: TextStyle = EntryItemDefaults.GridNameStyle,
    infoPadding: PaddingValues = EntryItemDefaults.GridPadding,
    badgeSize: Dp = EntryItemDefaults.GridBadgeSize,
    shape: Shape = EntryItemDefaults.GridShape,
    additionalContent: (@Composable () -> Unit)? = { AnimeGridItemDefaultAdditionalContent(anime) },
) {
    EntryGridItem(
        name = anime.name,
        russianName = anime.russianName,
        poster = anime.poster,
        onClick = onClick,
        modifier = modifier,
        userRateStatus = userRateStatus,
        colors = colors,
        nameStyle = nameStyle,
        infoPadding = infoPadding,
        badgeSize = badgeSize,
        shape = shape,
        additionalContent = additionalContent
    )
}

@Composable
@NonRestartableComposable
fun AnimeListItem(
    anime: Anime,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    colors: EntryItemColors = EntryItemDefaults.SurfaceContainerColors,
    posterWidth: Dp = EntryItemDefaults.ListItemPosterWidth,
    nameStyle: TextStyle = EntryItemDefaults.ListNameStyle,
    infoPadding: PaddingValues = EntryItemDefaults.ListPadding,
    badgeSize: Dp = EntryItemDefaults.ListBadgeSize,
    shape: Shape = EntryItemDefaults.ListShape,
    additionalContent: (@Composable () -> Unit)? = { AnimeListItemDefaultAdditionalContent(anime) },
) {
    EntryListItem(
        name = anime.name,
        russianName = anime.russianName,
        poster = anime.poster,
        onClick = onClick,
        modifier = modifier,
        userRateStatus = userRateStatus,
        colors = colors,
        posterWidth = posterWidth,
        nameStyle = nameStyle,
        infoPadding = infoPadding,
        badgeSize = badgeSize,
        shape = shape,
        additionalContent = additionalContent
    )
}

@Composable
@NonRestartableComposable
fun AnimeCarouselItem(
    anime: Anime,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    colors: EntryItemColors = EntryItemDefaults.SurfaceColors,
    nameStyle: TextStyle = EntryItemDefaults.CarouselNameStyle,
    infoPadding: PaddingValues = EntryItemDefaults.CarouselPadding,
    badgeSize: Dp = EntryItemDefaults.CarouselBadgeSize,
    shape: Shape = EntryItemDefaults.CarouselShape,
    additionalContent: (@Composable () -> Unit)? = null,
) {
    EntryCarouselItem(
        name = anime.name,
        russianName = anime.russianName,
        poster = anime.poster,
        onClick = onClick,
        modifier = modifier,
        userRateStatus = userRateStatus,
        colors = colors,
        nameStyle = nameStyle,
        infoPadding = infoPadding,
        badgeSize = badgeSize,
        shape = shape,
        additionalContent = additionalContent
    )
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

@Composable
@Preview
fun AnimeCarouselItemPreview(@PreviewParameter(AnimeItemPreviewParameterProvider::class) anime: Anime) {
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

@Preview
@Composable
private fun AnimeGridItemPreview_WithUserRateStatus(
    @PreviewParameter(ListOfAnimesPreviewParameterProvider::class) animes: List<Anime>
) {
    SeanimeTheme {
        ProvideTitleStringsByType(EntryType.Anime) {
            AnimeGridItem(
                anime = animes[0],
                onClick = {},
                modifier = Modifier.width(150.dp),
                userRateStatus = UserRateStatus.Watching
            )
        }
    }
}

@Preview
@Composable
private fun AnimeListItemPreview_WithUserRateStatus(
    @PreviewParameter(ListOfAnimesPreviewParameterProvider::class) animes: List<Anime>
) {
    SeanimeTheme {
        ProvideTitleStringsByType(EntryType.Anime) {
            AnimeListItem(
                anime = animes[0],
                onClick = {},
                userRateStatus = UserRateStatus.Watching
            )
        }
    }
}

@Preview
@Composable
private fun AnimeCarouselItemPreview_WithUserRateStatus(
    @PreviewParameter(ListOfAnimesPreviewParameterProvider::class) animes: List<Anime>
) {
    SeanimeTheme {
        ProvideTitleStringsByType(EntryType.Anime) {
            AnimeCarouselItem(
                anime = animes[0],
                onClick = {},
                userRateStatus = UserRateStatus.Watching
            )
        }
    }
}
