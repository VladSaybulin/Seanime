package ru.vladsaybulin.ui2.entry.related

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.components.SeanimeTag
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui2.entry.R
import ru.vladsaybulin.core.ui2.strings.compose.asStringOrNull
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.related.RelatedAnime
import ru.vladsaybulin.model.related.RelatedManga
import ru.vladsaybulin.model.related.RelatedTitle
import ru.vladsaybulin.model.related.RelationType
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.ui2.entry.EntryItemDefaults
import ru.vladsaybulin.ui2.entry.EntryListItem
import ru.vladsaybulin.ui2.entry.additional.AdditionalContentKindAndYear
import ru.vladsaybulin.ui2.entry.additional.AdditionalContentStatusTag

@Composable
fun RelatedTitleItem(
    relatedTitle: RelatedTitle,
    onClick: (EntryType, Long) -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None
) {
    val data = when (relatedTitle) {
        is RelatedAnime -> animeData(relatedTitle)
        is RelatedManga -> mangaData(relatedTitle)
    }

    EntryListItem(
        name = data.name,
        russianName = data.russianName,
        poster = data.poster,
        posterWidth = PosterWidth,
        onClick = { onClick(data.type, data.id) },
        modifier = modifier,
        userRateStatus = userRateStatus,
        style = EntryItemDefaults.regularListStyle(
            containerColor = SeanimeTheme.colorScheme.surface,
            colorsByUserRateStatus = false,
            shape = RoundedCornerShape(ShapeRadius)
        ),
        additionalContent = {
            RelatedTitleDetails(
                data.kindStr,
                data.year,
                data.status,
                data.relatedType
            )
        }
    )
}

@Composable
fun RelatedTitleDetails(kindStr: String?, year: Int?, status: EntryStatus, relationType: RelationType) {

    Column {
        AdditionalContentKindAndYear(kindStr, year)
        Spacer(Modifier.height(TagSpace))
        Row(horizontalArrangement = Arrangement.spacedBy(TagSpace)) {
            AdditionalContentStatusTag(status)
            SeanimeTag {
                Text(relationTypeString(relationType))
            }
        }
    }
}

@Composable
@ReadOnlyComposable
private fun relationTypeString(relationType: RelationType) = when (relationType) {
    RelationType.Adaptation -> R.string.core_ui2_entry_relation_type_adaptation
    RelationType.AltSetting -> R.string.core_ui2_entry_relation_type_alt_setting
    RelationType.AltHistory -> R.string.core_ui2_entry_relation_type_alt_history
    RelationType.SideStory -> R.string.core_ui2_entry_relation_type_side_story
    RelationType.FullStory -> R.string.core_ui2_entry_relation_type_full_story
    RelationType.ParentStory -> R.string.core_ui2_entry_relation_type_parent_history
    RelationType.Sequel -> R.string.core_ui2_entry_relation_type_sequel
    RelationType.Prequel -> R.string.core_ui2_entry_relation_type_prequel
    RelationType.Summary -> R.string.core_ui2_entry_relation_type_summary
    RelationType.Character -> R.string.core_ui2_entry_relation_type_character
    RelationType.SpinOff -> R.string.core_ui2_entry_relation_type_spin_off
    RelationType.Other -> R.string.core_ui2_entry_relation_type_other
}.let { stringResource(it) }

@Composable
@ReadOnlyComposable
private fun animeData(related: RelatedAnime): RelatedData {
    val anime = related.anime
    return RelatedData(
        type = EntryType.Anime,
        id = anime.id,
        name = anime.name,
        russianName = anime.russianName,
        poster = anime.poster,
        kindStr = anime.kind.asStringOrNull(),
        year = anime.airedOn?.year ?: related.anime.releasedOn?.year,
        status = anime.status,
        relatedType = related.relationType
    )
}

@Composable
@ReadOnlyComposable
private fun mangaData(related: RelatedManga): RelatedData {
    val manga = related.manga
    return RelatedData(
        type = EntryType.Anime,
        id = manga.id,
        name = manga.name,
        russianName = manga.russianName,
        poster = manga.poster,
        kindStr = manga.kind.asStringOrNull(),
        year = manga.airedOn?.year ?: related.manga.releasedOn?.year,
        status = manga.status,
        relatedType = related.relationType
    )
}

private class RelatedData(
    val type: EntryType,
    val id: Long,
    val name: String,
    val russianName: String?,
    val poster: Image?,
    val kindStr: String?,
    val year: Int?,
    val status: EntryStatus,
    val relatedType: RelationType
)

private val PosterWidth = 72.dp
private val TagSpace = 4.dp
private val ShapeRadius = 8.dp