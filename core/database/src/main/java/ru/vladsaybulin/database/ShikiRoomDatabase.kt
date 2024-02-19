package ru.vladsaybulin.database

import androidx.room.RoomDatabase
import androidx.room.withTransaction
import ru.vladsaybulin.database.dao.CalendarDao

internal abstract class ShikiRoomDatabase : RoomDatabase() {
    abstract fun calendarDao(): CalendarDao
}

class ShikiDatabase internal constructor(private val database: ShikiRoomDatabase) {

    val calendarDao: CalendarDao
        get() = database.calendarDao()

    suspend fun <R> withTransaction(block: suspend () -> R): R =
        database.withTransaction(block)
}