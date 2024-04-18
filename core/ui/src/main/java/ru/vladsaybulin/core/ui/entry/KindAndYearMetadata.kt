package ru.vladsaybulin.core.ui.entry

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import ru.vladsaybulin.core.ui.R

@Composable
fun EntryKindAndYearMetadata(
    entryKindString: String?,
    airedInYear: Int?,
    textStyle: TextStyle = LocalTextStyle.current
) {
    when {
        entryKindString != null && airedInYear != null -> Text(
            text = stringResource(
                id = R.string.kind_year_meta_data_text,
                entryKindString,
                airedInYear.toString()
            ),
            style = textStyle
        )

        entryKindString != null -> Text(
            text = stringResource(
                id = R.string.kind_meta_data_text,
                entryKindString
            ),
            style = textStyle
        )

        airedInYear != null -> Text(
            text = stringResource(
                id = R.string.year_meta_data_text,
                airedInYear
            ),
            style = textStyle
        )

        else -> Unit
    }
}