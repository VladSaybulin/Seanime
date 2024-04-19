package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.ui.EntryStatusBadge
import ru.vladsaybulin.feature.details.R
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.IncompleteDate
import ru.vladsaybulin.model.genre.Genre
import java.time.format.DateTimeFormatter

@Composable
internal fun StatusAndDatesInfoLine(
    airedOn: IncompleteDate?,
    releasedOn: IncompleteDate?,
    status: EntryStatus,
) {

    val dates = buildDatesString(airedOn, releasedOn, status == EntryStatus.Ongoing)

    if (status != EntryStatus.None || dates != null) {
        InfoLine(
            icon = { InfoIcon(imageVector = ShikimoriIcons.CalendarToday) }
        ) {
            if (status != EntryStatus.None) {
                EntryStatusBadge(status = status)
            }
            if (dates != null) {
                if (status != EntryStatus.None) {
                    Spacer(Modifier.width(4.dp))
                }
                Text(text = dates)
            }
        }
    }
}

@Composable
internal fun GenresInfoLine(
    genres: ImmutableList<Genre>,
    onGenreClick: (Genre) -> Unit,
) {
    ListedInformation(
        items = genres,
        labelSingleStringRes = R.string.genre,
        labelSeveralStringRes = R.string.genres,
        name = { it.russianName ?: it.englishName },
        onItemClick = onGenreClick
    )
}

@OptIn(ExperimentalTextApi::class)
@Composable
internal fun <T>ListedInformation(
    items: ImmutableList<T>,
    labelSingleStringRes: Int,
    labelSeveralStringRes: Int,
    name: (T) -> String,
    onItemClick: (T) -> Unit
) {
    InfoLine(icon = { InfoIconPlaceholder() }) {
        val textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current)
        val linkSpanStyle = textStyle.copy(textDecoration = TextDecoration.Underline).toSpanStyle()

        val annotatedString = buildAnnotatedString {
            if (items.size == 1) {
                append(stringResource(id = labelSingleStringRes))
            } else {
                append(stringResource(id = labelSeveralStringRes))
            }
            append(": ")

            var appendSeparator = false
            items.forEachIndexed { index, item ->
                if (appendSeparator) {
                    append(", ")
                } else {
                    appendSeparator = true
                }
                withAnnotation(tag = "item", annotation = index.toString()) {
                    withStyle(linkSpanStyle) {
                        append(name(item))
                    }
                }
            }
        }

        ClickableText(
            text = annotatedString,
            style = textStyle
        ) { offset ->
            val ranges = annotatedString.getStringAnnotations("item", offset, offset)
            ranges.firstOrNull()?.let {
                onItemClick(items[it.item.toInt()])
            }
        }
    }
}

@Composable
@ReadOnlyComposable
private fun buildDatesString(
    airedOn: IncompleteDate?,
    releasedOn: IncompleteDate?,
    ongoing: Boolean
): String? {
    val airedOnText = airedOn?.let { formatIncompleteDateToString(date = it) }
    val releasedOnText = releasedOn?.let { date ->
        if (date != airedOn) {
            formatIncompleteDateToString(date = date)
        } else null
    }

    return when {
        airedOnText != null && releasedOnText != null -> stringResource(
            id = R.string.dates_range,
            airedOnText,
            releasedOnText
        )

        airedOnText != null && ongoing -> stringResource(id = R.string.start_date, airedOnText)
        airedOnText != null -> airedOnText
        else -> null
    }
}

@Composable
@ReadOnlyComposable
private fun formatIncompleteDateToString(date: IncompleteDate): String? = with(date) {
    val pattern = stringResource(
        id = when {
            year != null && month != null && day != null -> R.string.date_formatter_pattern_full
            year != null && month != null -> R.string.date_formatter_without_days
            year != null -> R.string.date_formatter_pattern_only_year
            else -> return@with null
        }
    )
    val localDate = LocalDate(
        dayOfMonth = date.month ?: 1,
        monthNumber = date.month ?: 1,
        year = date.year!!
    )
    return@with DateTimeFormatter.ofPattern(pattern).format(localDate.toJavaLocalDate())
}