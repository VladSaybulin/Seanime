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

package ru.vladsaybulin.core.ui2.entry.additional

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui2.score.Score
import ru.vladsaybulin.core.ui2.strings.compose.ProvideTitleStringsByType
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType

@Composable
fun TitleGridItemAdditionalContent(
    kindStr: String?,
    year: Int?,
    textStyle: TextStyle = TitleGridDefaultAdditionalInfoStyle
) {
    ProvideTextStyle(textStyle) {
        AdditionalContentKindAndYear(kindStr, year)
    }
}

@Composable
fun TitleListItemDefaultAdditionalContent(
    status: EntryStatus,
    year: Int?,
    kindAndVolumes: String?,
    score: Float = 0f,
    textStyle: TextStyle = TitleListDefaultAdditionalInfoStyle
) {
    ProvideTextStyle(textStyle) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AdditionalContentStatusTag(
                    status = status,
                    modifier = Modifier.padding(StatusTagPadding)
                )
                if (year != null) {
                    Text(text = year.toString())
                }
            }
            if (kindAndVolumes != null) {
                Text(text = kindAndVolumes)
            }
            if (score > 0) {
                Score(
                    value = score,
                    iconSize = 16.dp,
                    numberStyle = LocalTextStyle.current
                )
            }
        }
    }
}

@Preview
@Composable
private fun TitleListItemAdditionalContentMeasurePolicy_Full() {
    SeanimeTheme {
        Surface(color = SeanimeTheme.colorScheme.surfaceContainer) {
            ProvideTitleStringsByType(EntryType.Anime) {
                TitleListItemDefaultAdditionalContent(
                    status = EntryStatus.Ongoing,
                    year = 2024,
                    kindAndVolumes = "TV, 12 eps"
                )
            }
        }
    }
}

@Preview
@Composable
private fun TitleListItemAdditionalContentMeasurePolicy_WithoutStatus() {
    SeanimeTheme {
        Surface(color = SeanimeTheme.colorScheme.surfaceContainer) {
            ProvideTitleStringsByType(EntryType.Anime) {
                TitleListItemDefaultAdditionalContent(
                    status = EntryStatus.None,
                    year = 2024,
                    kindAndVolumes = "TV, 12 eps"
                )
            }
        }
    }
}

@Preview
@Composable
private fun TitleListItemAdditionalContentMeasurePolicy_WithoutYear() {
    SeanimeTheme {
        Surface(color = SeanimeTheme.colorScheme.surfaceContainer) {
            ProvideTitleStringsByType(EntryType.Anime) {
                TitleListItemDefaultAdditionalContent(
                    status = EntryStatus.Ongoing,
                    year = null,
                    kindAndVolumes = "TV, 12 eps"
                )
            }
        }
    }
}

@Preview
@Composable
private fun TitleListItemAdditionalContentMeasurePolicy_WithoutKindAndVolumes() {
    SeanimeTheme {
        Surface(color = SeanimeTheme.colorScheme.surfaceContainer) {
            ProvideTitleStringsByType(EntryType.Anime) {
                TitleListItemDefaultAdditionalContent(
                    status = EntryStatus.Ongoing,
                    year = 2024,
                    kindAndVolumes = null
                )
            }
        }
    }
}

@Preview
@Composable
private fun TitleListItemAdditionalContentMeasurePolicy_StatusOnly() {
    SeanimeTheme {
        Surface(color = SeanimeTheme.colorScheme.surfaceContainer) {
            ProvideTitleStringsByType(EntryType.Anime) {
                TitleListItemDefaultAdditionalContent(
                    status = EntryStatus.Ongoing,
                    year = null,
                    kindAndVolumes = null
                )
            }
        }
    }
}

@Preview
@Composable
private fun TitleListItemAdditionalContentMeasurePolicy_YearOnly() {
    SeanimeTheme {
        Surface(color = SeanimeTheme.colorScheme.surfaceContainer) {
            ProvideTitleStringsByType(EntryType.Anime) {
                TitleListItemDefaultAdditionalContent(
                    status = EntryStatus.None,
                    year = 2024,
                    kindAndVolumes = null
                )
            }
        }
    }
}

@Preview
@Composable
private fun TitleListItemAdditionalContentMeasurePolicy_KindAndEpisodesOnly() {
    SeanimeTheme {
        Surface(color = SeanimeTheme.colorScheme.surfaceContainer) {
            ProvideTitleStringsByType(EntryType.Anime) {
                TitleListItemDefaultAdditionalContent(
                    status = EntryStatus.None,
                    year = null,
                    kindAndVolumes = "TV, 12 eps"
                )
            }
        }
    }
}

private val StatusSpace = 4.dp
private val StatusTagPadding = PaddingValues(top = StatusSpace, end = StatusSpace, bottom = StatusSpace)
private const val TITLE_GRID_DEFAULT_ADDITIONAL_CONTENT_OPACITY = 0.67f

private val TitleGridDefaultAdditionalInfoStyle: TextStyle
    @Composable @ReadOnlyComposable get() {
        val textStyle = SeanimeTheme.typography.labelSmall
        val color = LocalContentColor.current.copy(alpha = TITLE_GRID_DEFAULT_ADDITIONAL_CONTENT_OPACITY)
        return textStyle.copy(color = color)
    }

private val TitleListDefaultAdditionalInfoStyle: TextStyle
    @Composable @ReadOnlyComposable get() = SeanimeTheme.typography.labelSmall
