package ru.vladsaybulin.database.models.calendar

import androidx.room.Embedded
import androidx.room.Relation
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.anime.asExternalModel
import ru.vladsaybulin.model.calendar.CalendarItem
import kotlin.time.Duration.Companion.minutes

class PopulatedCalendarItem(
    @Embedded
    val calendarItemEntity: CalendarItemEntity,

    @Relation(
        entity = AnimeEntity::class,
        parentColumn = "anime_id",
        entityColumn = "id"
    )
    val animeDbo: AnimeEntity
)

fun PopulatedCalendarItem.asExternalModel() =
    CalendarItem(
        anime = animeDbo.asExternalModel(),
        nextEpisode = calendarItemEntity.nextEpisode,
        nextEpisodeAt = calendarItemEntity.nextEpisodeAt,
        duration = calendarItemEntity.duration?.minutes
    )
