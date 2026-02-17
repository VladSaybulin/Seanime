package ru.vladsaybulin.ui2.entry.additional

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    year: Int?
) {
    AdditionalContentKindAndYear(kindStr, year)
}

@Composable
fun TitleListItemDefaultAdditionalContent(
    status: EntryStatus,
    year: Int?,
    kindAndVolumes: String?,
    score: Float = 0f,
) {

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