package ru.vladsaybulin.core.ui.entry

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.ui.EntryStatusBadge
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.IncompleteDate
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun EntryInfoKindAndYear(
    kindText: String?,
    year: Int?
) {
    val text = when {
        kindText != null && year != null ->
            stringResource(
                id = R.string.core_ui_metadata_kind_and_year,
                kindText,
                year
            )

        year != null -> stringResource(id = R.string.core_ui_metadata_year)
        kindText != null -> kindText
        else -> null
    }

    if (text != null) {
        Text(text)
    }
}

@Composable
fun EntryInfoScore(score: Float) {
    if (score == 0f) return

    val scoreText = DecimalFormat("#.##").format(score)

    Row {
        Icon(
            imageVector = ShikimoriIcons.Star,
            contentDescription = null
        )

        Text(text = scoreText)
    }
}

@Composable
fun EntryInfoStatusAndDatesText(
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
@ReadOnlyComposable
fun incompleteDateFormatted(
    date: IncompleteDate,
    isGenitive: Boolean
): String? {
    val pattern = when {
        date.year != null && date.month != null && date.day != null ->
            if (isGenitive) {
                R.string.core_ui_incomplete_date_full_genitive
            } else R.string.core_ui_incomplete_date_full

        date.year != null && date.month != null ->
            if (isGenitive) {
                R.string.core_ui_incomplete_date_year_month_genitive
            } else R.string.core_ui_incomplete_date_year_month

        date.year != null -> if (isGenitive) {
            R.string.core_ui_incomplete_date_year_genitive
        } else R.string.core_ui_incomplete_date_year

        else -> null
    }?.let { stringResource(id = it) }

    return if (pattern != null) {
        DateTimeFormatter.ofPattern(pattern).format(date.asLocalDate())
    } else null
}

private fun IncompleteDate.asLocalDate() =
    LocalDate.of(year ?: 1, month ?: 1, day ?: 1)