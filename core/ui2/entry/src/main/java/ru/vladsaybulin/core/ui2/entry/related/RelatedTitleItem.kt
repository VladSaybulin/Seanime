/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vladsaybulin.core.ui2.entry.related

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.components.SeanimeTag
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui2.entry.R
import ru.vladsaybulin.core.ui2.strings.compose.ProvideTitleStringsByType
import ru.vladsaybulin.core.ui2.strings.compose.asStringOrNull
import ru.vladsaybulin.model.title.TitleStatus
import ru.vladsaybulin.model.related.RelatedTitle
import ru.vladsaybulin.model.related.RelationType
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.core.ui2.entry.EntryItemDefaults
import ru.vladsaybulin.core.ui2.entry.EntryListItem
import ru.vladsaybulin.core.ui2.entry.additional.AdditionalContentKindAndYear
import ru.vladsaybulin.core.ui2.entry.additional.AdditionalContentStatusTag
import ru.vladsaybulin.model.title.Title

@Composable
fun RelatedTitleItem(
    relatedTitle: RelatedTitle,
    onClick: (Title) -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None
) {
    RelatedTitleItem(
        title = relatedTitle.title,
        relationType = relatedTitle.relationType,
        onClick = onClick,
        modifier = modifier,
        userRateStatus = userRateStatus
    )
}

@Composable
private fun RelatedTitleItem(
    title: Title,
    relationType: RelationType,
    onClick: (Title) -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None
) {
    ProvideTitleStringsByType(title.type) {
        EntryListItem(
            name = title.name,
            russianName = title.nameRu,
            poster = title.poster,
            posterWidth = PosterWidth,
            onClick = { onClick(title) },
            modifier = modifier,
            userRateStatus = userRateStatus,
            colors = EntryItemDefaults.SurfaceColors,
            additionalContent = {
                RelatedTitleDetails(
                    title.kind.asStringOrNull(),
                    title.airedOn?.year ?: title.releasedOn?.year,
                    title.status,
                    relationType
                )
            }
        )
    }
}

@Composable
fun RelatedTitleDetails(kindStr: String?, year: Int?, status: TitleStatus, relationType: RelationType) {
    ProvideTextStyle(SeanimeTheme.typography.labelSmall) {
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

private val PosterWidth = 80.dp
private val TagSpace = 4.dp