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
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
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