package ru.vladsaybulin.feature.details

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vladsaybulin.feature.details.info.AnimeKindEpisodeLine
import ru.vladsaybulin.feature.details.info.GenresLine
import ru.vladsaybulin.feature.details.info.NextEpisodeLine
import ru.vladsaybulin.feature.details.info.StatusDatesLine
import ru.vladsaybulin.feature.details.info.StudiosLine
import ru.vladsaybulin.feature.details.model.DetailsInfo

@Composable
fun DetailsInfoLine(
    info: DetailsInfo,
    modifier: Modifier = Modifier,
) {
    when (info) {
        is DetailsInfo.AnimeKindEpisodes -> AnimeKindEpisodeLine(info, modifier)
        is DetailsInfo.Genres -> GenresLine(info, modifier)
        is DetailsInfo.NextEpisode -> NextEpisodeLine(info, modifier)
        is DetailsInfo.StatusDates -> StatusDatesLine(info, modifier)
        is DetailsInfo.Studios -> StudiosLine(info, modifier)
    }
}