package ru.vladsaybulin.feature.details.info

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.EntryStatusBadge
import ru.vladsaybulin.feature.details.R
import ru.vladsaybulin.feature.details.model.DetailsInfo
import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.IncompleteDate
import java.time.format.DateTimeFormatter

@Composable
fun StatusDatesLine(
    info: DetailsInfo.StatusDates,
    modifier: Modifier = Modifier
) {
    InfoLine(
        icon = { InfoIcon(imageVector = ShikimoriIcons.CalendarToday) },
        modifier = modifier
    ) {
        if (info.status != EntryStatus.None) {
            EntryStatusBadge(status = info.status, entryType = info.entryType)
        }

        buildDatesString(
            airedOn = info.airedOn,
            releasedOn = info.releasedOn,
            ongoing = info.status == EntryStatus.Ongoing
        )?.let {
            if (info.status != EntryStatus.None) {
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(text = it)
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

@Composable
@Preview
fun EntryStatusDatesPreview(
    @PreviewParameter(DetailsInfoStatusDatesPreviewProvider::class) info: DetailsInfo.StatusDates
) {
    ShikimoriTheme {
        Surface {
            StatusDatesLine(info = info)
        }
    }
}

class DetailsInfoStatusDatesPreviewProvider :
    PreviewParameterProvider<DetailsInfo.StatusDates> {
    override val values: Sequence<DetailsInfo.StatusDates>
        get() = sequenceOf(
            DetailsInfo.StatusDates(
                entryType = EntryType.Anime,
                status = EntryStatus.Released,
                airedOn = IncompleteDate(1, 1, 2024),
                releasedOn = IncompleteDate(2, 1, 2024)
            ),
            DetailsInfo.StatusDates(
                entryType = EntryType.Anime,
                status = EntryStatus.Released,
                airedOn = IncompleteDate(1, 1, 2024),
                releasedOn = null
            ),
            DetailsInfo.StatusDates(
                entryType = EntryType.Anime,
                status = EntryStatus.Ongoing,
                airedOn = IncompleteDate(1, 1, 2024),
                releasedOn = null
            ),
            DetailsInfo.StatusDates(
                entryType = EntryType.Anime,
                status = EntryStatus.Anons,
                airedOn = null,
                releasedOn = null
            )
        )
}