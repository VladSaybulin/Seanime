package ru.vladsaybulin.feature.title.details.content

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.components.ShikimoriTextBadge
import ru.vladsaybulin.core.ui.EntryStatusBadge
import ru.vladsaybulin.core.ui.anime.AnimeInfoKindAndYearText
import ru.vladsaybulin.core.ui.entry.EntryInfoScore
import ru.vladsaybulin.core.ui.entry.EntryListItem
import ru.vladsaybulin.core.ui.manga.MangaInfoKindAndYearText
import ru.vladsaybulin.feature.title.details.R
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.related.RelatedEntry
import ru.vladsaybulin.model.related.RelationType

@Composable
internal fun TitleRelatedItem(
    relatedEntry: RelatedEntry,
    onAnimeClick: (Anime) -> Unit,
    onMangaClick: (Manga) -> Unit,
    modifier: Modifier = Modifier
) {
    if (relatedEntry.anime != null) {
        val anime = relatedEntry.anime!!
        RelatedAnimeListItem(
            anime = anime,
            relationType = relatedEntry.relationType,
            onClick = { onAnimeClick(anime) },
            modifier = modifier
        )
    } else {
        val manga = relatedEntry.manga!!
        RelatedMangaListItem(
            manga = manga,
            relationType = relatedEntry.relationType,
            onClick = { onMangaClick(manga) },
            modifier = modifier
        )
    }
}

@Composable
private fun RelatedAnimeListItem(
    anime: Anime,
    relationType: RelationType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    EntryListItem(
        name = anime.run { russianName ?: name },
        imageUrl = anime.poster?.previewUrl,
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        imageWidth = 72.dp,
        imageIgnoresPadding = true,
        metadata = {
            AnimeInfoKindAndYearText(kind = anime.kind, year = anime.airedOn?.year)

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

                ShikimoriTextBadge {
                    Text(relationTypeString(relationType))
                }
            }

            EntryInfoScore(score = anime.score ?: 0f)
        }
    )
}

@Composable
private fun RelatedMangaListItem(
    manga: Manga,
    relationType: RelationType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    EntryListItem(
        name = manga.run { russianName ?: name },
        imageUrl = manga.poster?.previewUrl,
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        imageWidth = 72.dp,
        imageIgnoresPadding = true,
        metadata = {
            MangaInfoKindAndYearText(kind = manga.kind, year = manga.airedOn?.year)

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

                ShikimoriTextBadge {
                    Text(relationTypeString(relationType))
                }
            }

            EntryInfoScore(score = manga.score ?: 0f)
        }
    )
}

@Composable
@ReadOnlyComposable
private fun relationTypeString(relationType: RelationType) =
    stringResource(id = relationTypeStringResId(relationType))

private fun relationTypeStringResId(relationType: RelationType) = when (relationType) {
    RelationType.Adaptation -> R.string.feature_title_details_relation_type_adaptation
    RelationType.AltSetting -> R.string.feature_title_details_relation_type_alt_setting
    RelationType.AltHistory -> R.string.feature_title_details_relation_type_alt_history
    RelationType.SideStory -> R.string.feature_title_details_relation_type_side_story
    RelationType.FullStory -> R.string.feature_title_details_relation_type_full_story
    RelationType.ParentStory -> R.string.feature_title_details_relation_type_parent_history
    RelationType.Sequel -> R.string.feature_title_details_relation_type_sequel
    RelationType.Prequel -> R.string.feature_title_details_relation_type_prequel
    RelationType.Summary -> R.string.feature_title_details_relation_type_summary
    RelationType.Character -> R.string.feature_title_details_relation_type_character
    RelationType.SpinOff -> R.string.feature_title_details_relation_type_spin_off
    RelationType.Other -> R.string.feature_title_details_relation_type_other
}