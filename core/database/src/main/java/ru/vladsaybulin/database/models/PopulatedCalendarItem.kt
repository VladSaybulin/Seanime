package ru.vladsaybulin.database.models

import androidx.room.Embedded
import androidx.room.Relation

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