package ru.vladsaybulin.core.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.components.SeanimeTag
import ru.vladsaybulin.core.ui.entry.list.EntryListItem
import ru.vladsaybulin.core.ui.entry.metadata.AnimeMetadataDefaultComponents
import ru.vladsaybulin.core.ui.entry.metadata.MangaMetadataComponents
import ru.vladsaybulin.core.ui.entry.metadata.TitleMetadataComponents
import ru.vladsaybulin.core.ui.strings.LocalTitleStrings
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.related.RelatedAnime
import ru.vladsaybulin.model.related.RelatedManga
import ru.vladsaybulin.model.related.RelatedTitle
import ru.vladsaybulin.model.related.RelationType

@Composable
fun RelatedTitleItem(
    relatedTitle: RelatedTitle,
    onClick: (EntryType, Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (relatedTitle) {
        is RelatedAnime -> RelatedAnimeItem(
            anime = relatedTitle.anime,
            relationType = relatedTitle.relationType,
            onClick = { onClick(EntryType.Anime, relatedTitle.anime.id) },
            modifier = modifier
        )
        is RelatedManga -> RelatedMangaItem(
            manga = relatedTitle.manga,
            relationType = relatedTitle.relationType,
            onClick = { onClick(EntryType.Manga, relatedTitle.manga.id) },
            modifier = modifier
        )
    }
}

@Composable
private fun RelatedAnimeItem(
    anime: Anime,
    relationType: RelationType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(value = LocalTitleStrings provides EntryType.Anime) {
        EntryListItem(
            name = anime.run { russianName ?: name },
            imageUrl = anime.poster?.previewUrl,
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            imageWidth = 72.dp,
            imageIgnoresPadding = true,
            metadata = {
                AnimeMetadataDefaultComponents.KindAndYear(anime)

                Row {
                    if (anime.status != EntryStatus.None) {
                        EntryStatusBadge(
                            status = anime.status,
                            modifier = Modifier.padding(
                                top = 2.dp,
                                bottom = 2.dp,
                                end = 4.dp
                            )
                        )
                    }

                    SeanimeTag {
                        Text(relationTypeString(relationType))
                    }
                }

                TitleMetadataComponents.Score(score = anime.score)
            }
        )
    }
}

@Composable
private fun RelatedMangaItem(
    manga: Manga,
    relationType: RelationType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(value = LocalTitleStrings provides EntryType.Manga) {
        EntryListItem(
            name = manga.run { russianName ?: name },
            imageUrl = manga.poster?.previewUrl,
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            imageWidth = 72.dp,
            imageIgnoresPadding = true,
            metadata = {
                MangaMetadataComponents.KindAndYearLine(manga)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (manga.status != EntryStatus.None) {
                        EntryStatusBadge(
                            status = manga.status,
                            modifier = Modifier.padding(
                                top = 2.dp,
                                bottom = 2.dp,
                                end = 4.dp
                            ),
                        )
                    }

                    SeanimeTag {
                        Text(
                            text = relationTypeString(relationType),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                TitleMetadataComponents.Score(score = manga.score)
            }
        )
    }
}

@Composable
@ReadOnlyComposable
private fun relationTypeString(relationType: RelationType) =
    stringResource(id = relationTypeStringResId(relationType))

private fun relationTypeStringResId(relationType: RelationType) = when (relationType) {
    RelationType.Adaptation -> R.string.core_ui_relation_type_adaptation
    RelationType.AltSetting -> R.string.core_ui_relation_type_alt_setting
    RelationType.AltHistory -> R.string.core_ui_relation_type_alt_history
    RelationType.SideStory -> R.string.core_ui_relation_type_side_story
    RelationType.FullStory -> R.string.core_ui_relation_type_full_story
    RelationType.ParentStory -> R.string.core_ui_relation_type_parent_history
    RelationType.Sequel -> R.string.core_ui_relation_type_sequel
    RelationType.Prequel -> R.string.core_ui_relation_type_prequel
    RelationType.Summary -> R.string.core_ui_relation_type_summary
    RelationType.Character -> R.string.core_ui_relation_type_character
    RelationType.SpinOff -> R.string.core_ui_relation_type_spin_off
    RelationType.Other -> R.string.core_ui_relation_type_other
}