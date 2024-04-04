package ru.vladsaybulin.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.withTransaction
import ru.vladsaybulin.database.dao.AnimeDao
import ru.vladsaybulin.database.dao.CalendarDao
import ru.vladsaybulin.database.dao.MangaDao
import ru.vladsaybulin.database.dao.UserRateDao
import ru.vladsaybulin.database.models.AnimeDbo
import ru.vladsaybulin.database.models.CalendarItemDbo
import ru.vladsaybulin.database.models.MangaDbo
import ru.vladsaybulin.database.models.UserRateDbo
import ru.vladsaybulin.database.utils.AnimeKindTypeConverter
import ru.vladsaybulin.database.utils.EntryStatusTypeConverter
import ru.vladsaybulin.database.utils.InstantTypeConverter
import ru.vladsaybulin.database.utils.MangaKindTypeConverter
import ru.vladsaybulin.database.utils.UserRateTypeConverter

@Database(
    entities = [
        CalendarItemDbo::class,
        AnimeDbo::class,
        MangaDbo::class,
        UserRateDbo::class
    ],
    version = 1,
)
@TypeConverters(
    value = [
        AnimeKindTypeConverter::class,
        MangaKindTypeConverter::class,
        EntryStatusTypeConverter::class,
        InstantTypeConverter::class,
        UserRateTypeConverter::class
    ]
)
internal abstract class ShikiRoomDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao
    abstract fun mangaDao(): MangaDao
    abstract fun calendarDao(): CalendarDao
    abstract fun userRateDao(): UserRateDao
}

class ShikiDatabase internal constructor(private val database: ShikiRoomDatabase) {

    val animeDao: AnimeDao
        get() = database.animeDao()

    val mangaDao: MangaDao
        get() = database.mangaDao()

    val calendarDao: CalendarDao
        get() = database.calendarDao()

    val userRateDao: UserRateDao
        get() = database.userRateDao()

    suspend fun <R> withTransaction(block: suspend () -> R): R =
        database.withTransaction(block)
}