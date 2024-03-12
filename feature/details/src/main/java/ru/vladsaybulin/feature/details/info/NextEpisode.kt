package ru.vladsaybulin.feature.details.info

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toJavaZoneId
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.ui.LocalTimeZone
import ru.vladsaybulin.feature.details.R
import ru.vladsaybulin.feature.details.model.DetailsInfo
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
internal fun NextEpisodeLine(
    info: DetailsInfo.NextEpisode,
    modifier: Modifier = Modifier
) {
    InfoLine(
        icon = { InfoIcon(imageVector = ShikimoriIcons.AccessTime) },
        modifier = modifier
    ) {
        Text(text = dateFormatted(date = info.nextEpisodeDate))
    }
}

@Composable
private fun dateFormatted(date: Instant): String {
    val timeZone = LocalTimeZone.current
    val now = Clock.System.todayIn(timeZone)
    val localDateTime = date.toLocalDateTime(timeZone)
    val pattern = if (now.year == localDateTime.date.year) {
        stringResource(id = R.string.next_episode_at_pattern)
    } else {
        stringResource(id = R.string.next_episode_at_pattern_with_year)
    }
    return DateTimeFormatter
        .ofPattern(pattern)
        .withLocale(Locale.getDefault())
        .withZone(timeZone.toJavaZoneId())
        .format(localDateTime.toJavaLocalDateTime())
}