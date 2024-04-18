package ru.vladsaybulin.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.withTransaction
import ru.vladsaybulin.database.dao.AnimeDao
import ru.vladsaybulin.database.dao.CalendarDao
import ru.vladsaybulin.database.dao.GenreDao
import ru.vladsaybulin.database.dao.MangaDao
import ru.vladsaybulin.database.dao.OngoingAnimeDao
import ru.vladsaybulin.database.dao.PublisherDao
import ru.vladsaybulin.database.dao.RecentSearchQueriesDao
import ru.vladsaybulin.database.dao.StudioDao
import ru.vladsaybulin.database.dao.TopicsDao
import ru.vladsaybulin.database.dao.UserRateDao
import ru.vladsaybulin.database.dao.UsersDao
import ru.vladsaybulin.database.models.user.UserEntity
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.anime.OngoingAnimeEntity
import ru.vladsaybulin.database.models.anime.StudioEntity
import ru.vladsaybulin.database.models.calendar.CalendarItemEntity
import ru.vladsaybulin.database.models.genre.GenreEntity
import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.database.models.manga.PublisherEntity
import ru.vladsaybulin.database.models.search.RecentSearchQueryEntity
import ru.vladsaybulin.database.models.topic.TopicEntity
import ru.vladsaybulin.database.models.userrate.UserRateEntity
import ru.vladsaybulin.database.models.userrate.UserRateOrderDbo
import ru.vladsaybulin.database.utils.AnimeKindTypeConverter
import ru.vladsaybulin.database.utils.EntryStatusTypeConverter
import ru.vladsaybulin.database.utils.GenreKindTypeConverter
import ru.vladsaybulin.database.utils.InstantTypeConverter
import ru.vladsaybulin.database.utils.MangaKindTypeConverter
import ru.vladsaybulin.database.utils.TopicEventTypeConverter
import ru.vladsaybulin.database.utils.TopicLinkedTypeTypeConverter
import ru.vladsaybulin.database.utils.TopicTypeTypeConverter
import ru.vladsaybulin.database.utils.UserRateTypeConverter
import javax.inject.Inject

@Database(
    entities = [
        CalendarItemEntity::class,
        AnimeEntity::class,
        MangaEntity::class,
        UserRateEntity::class,
        UserEntity::class,
        TopicEntity::class,
        OngoingAnimeEntity::class,
        UserRateOrderDbo::class,
        RecentSearchQueryEntity::class,
        GenreEntity::class,
        StudioEntity::class,
        PublisherEntity::class
    ],
    version = 1,
)
@TypeConverters(
    value = [
        AnimeKindTypeConverter::class,
        MangaKindTypeConverter::class,
        EntryStatusTypeConverter::class,
        InstantTypeConverter::class,
        UserRateTypeConverter::class,
        TopicTypeTypeConverter::class,
        TopicLinkedTypeTypeConverter::class,
        TopicEventTypeConverter::class,
        GenreKindTypeConverter::class
    ]
)
abstract class ShikiRoomDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao
    abstract fun mangaDao(): MangaDao
    abstract fun calendarDao(): CalendarDao
    abstract fun userRateDao(): UserRateDao
    abstract fun topicsDao(): TopicsDao
    abstract fun usersDao(): UsersDao
    abstract fun ongoingAnimeDao(): OngoingAnimeDao
    abstract fun recentSearchQueriesDao(): RecentSearchQueriesDao
    abstract fun genreDao(): GenreDao
    abstract fun studioDao(): StudioDao
    abstract fun publisherDao(): PublisherDao
}

class ShikiDatabase @Inject constructor(private val database: ShikiRoomDatabase) {

    val animeDao: AnimeDao
        get() = database.animeDao()

    val mangaDao: MangaDao
        get() = database.mangaDao()

    val calendarDao: CalendarDao
        get() = database.calendarDao()

    val userRateDao: UserRateDao
        get() = database.userRateDao()

    val topicsDao: TopicsDao
        get() = database.topicsDao()

    val usersDao: UsersDao
        get() = database.usersDao()

    val ongoingAnimeDao: OngoingAnimeDao
        get() = database.ongoingAnimeDao()

    val recentSearchQueriesDao: RecentSearchQueriesDao
        get() = database.recentSearchQueriesDao()

    suspend fun <R> withTransaction(block: suspend () -> R): R =
        database.withTransaction(block)
}