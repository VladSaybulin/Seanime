package ru.vladsaybulin.core.ui.entry.metadata

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.ui.EntryStatusBadge
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.core.ui.entry.incompleteDateFormatted
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.IncompleteDate
import java.text.DecimalFormat

object TitleMetadataComponents {

    @Composable
    fun KindAndYearLine(kindStringId: Int?, year: Int?) {
        val kindString = kindStringId?.let { stringResource(id = it) }

        val text = when {
            kindString != null && year != null ->
                stringResource(
                    id = R.string.core_ui_metadata_kind_and_year,
                    kindString,
                    year
                )

            year != null -> stringResource(id = R.string.core_ui_metadata_year)
            kindString != null -> kindString
            else -> null
        }

        if (text != null) {
            Text(text)
        }
    }

    @Composable
    fun TitleStatusAndDatesRow(
        entryStatus: EntryStatus,
        airedOn: IncompleteDate?,
        releasedOn: IncompleteDate?
    ) {
        val datesText = if (airedOn != null || releasedOn != null) {
            val isGenitive = entryStatus == EntryStatus.Ongoing ||
                    (airedOn != null && releasedOn != null && airedOn != releasedOn)

            val airedText = airedOn?.let { incompleteDateFormatted(date = it, isGenitive = isGenitive) }
            val releasedText = releasedOn?.let { incompleteDateFormatted(date = it, isGenitive = isGenitive) }

            when {
                airedText != null && releasedText != null ->
                    stringResource(id = R.string.core_ui_info_dates_range, airedText, releasedText)

                airedText != null && entryStatus == EntryStatus.Ongoing ->
                    stringResource(id = R.string.core_ui_info_dates_from, airedText)

                airedText != null -> airedText
                releasedText != null -> releasedText
                else -> null
            }
        } else null

        if (entryStatus != EntryStatus.None || datesText != null) {
            Row {
                if (entryStatus != EntryStatus.None) {
                    EntryStatusBadge(
                        status = entryStatus,
                        modifier = Modifier.padding(
                            top = 2.dp,
                            bottom = 2.dp,
                            end = 4.dp
                        )
                    )
                }
                if (datesText != null) {
                    Text(text = datesText)
                }
            }
        }
    }

    @Composable
    fun Score(score: Float) {
        if (score == 0f) return

        val scoreText = DecimalFormat("#.##").format(score)

        Row {
            Icon(
                imageVector = SeanimeIcons.Star,
                contentDescription = null
            )

            Text(text = scoreText)
        }
    }
}