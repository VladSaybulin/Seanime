package ru.vladsaybulin.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.withTransaction
import ru.vladsaybulin.database.dao.CalendarDao
import ru.vladsaybulin.database.models.AnimeDbo
import ru.vladsaybulin.database.models.CalendarItemDbo
import ru.vladsaybulin.database.utils.AnimeKindTypeConverter
import ru.vladsaybulin.database.utils.EntryStatusTypeConverter
import ru.vladsaybulin.database.utils.InstantTypeConverter

@Database(
    entities = [
        CalendarItemDbo::class,
        AnimeDbo::class
    ],
    version = 1,
)
@TypeConverters(
    value = [
        AnimeKindTypeConverter::class,
        EntryStatusTypeConverter::class,
        InstantTypeConverter::class
    ]
)
internal abstract class ShikiRoomDatabase : RoomDatabase() {
    abstract fun calendarDao(): CalendarDao
}

class ShikiDatabase internal constructor(private val database: ShikiRoomDatabase) {

    val calendarDao: CalendarDao
        get() = database.calendarDao()

    suspend fun <R> withTransaction(block: suspend () -> R): R =
        database.withTransaction(block)
}