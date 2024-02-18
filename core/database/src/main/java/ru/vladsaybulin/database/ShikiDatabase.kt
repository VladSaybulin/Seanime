package ru.vladsaybulin.database

import androidx.room.RoomDatabase
import androidx.room.withTransaction
import ru.vladsaybulin.database.dao.CalendarDao

abstract class ShikiDatabase : RoomDatabase() {
    abstract fun calendarDao(): CalendarDao
}

class DatabaseTransaction internal constructor(private val database: ShikiDatabase) {
    suspend operator fun <R> invoke(block: suspend () -> R): R =
        database.withTransaction(block)
}