package ru.vladsaybulin.database.models

import androidx.room.Embedded
import androidx.room.Relation
import ru.vladsaybulin.model.Anime
import ru.vladsaybulin.model.CalendarItem
import kotlin.time.Duration.Companion.minutes

class PopulatedCalendarItem(
    @Embedded
    val calendarItemDbo: CalendarItemDbo,

    @Relation(
        entity = AnimeDbo::class,
        parentColumn = "anime_id",
        entityColumn = "id"
    )
    val animeDbo: AnimeDbo
)

fun PopulatedCalendarItem.asExternalModel() =
    CalendarItem(
        anime = Anime(
            id = animeDbo.id,
            originalName = animeDbo.originalName,
            russianName = animeDbo.russianName,
            poster = animeDbo.poster?.asExternalModel(),
            kind = animeDbo.kind,
            status = animeDbo.status,
            score = animeDbo.score,
            episodes = animeDbo.episodes,
            episodesAired = animeDbo.episodesAired,
            airedOn = animeDbo.airedOn?.asExternalModel(),
            releasedOn = animeDbo.releasedOn?.asExternalModel()
        ),
        nextEpisode = calendarItemDbo.nextEpisode,
        nextEpisodeAt = calendarItemDbo.nextEpisodeAt,
        duration = calendarItemDbo.duration?.minutes
    )
