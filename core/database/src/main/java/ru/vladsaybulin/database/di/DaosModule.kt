package ru.vladsaybulin.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.vladsaybulin.database.ShikiRoomDatabase
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

@Module
@InstallIn(SingletonComponent::class)
class DaosModule {

    @Provides
    fun provideAnimeDao(database: ShikiRoomDatabase): AnimeDao =
        database.animeDao()

    @Provides
    fun provideOngoingAnimesDao(database: ShikiRoomDatabase): OngoingAnimeDao =
        database.ongoingAnimeDao()

    @Provides
    fun provideCalendarDao(database: ShikiRoomDatabase): CalendarDao =
        database.calendarDao()

    @Provides
    fun provideMangaDao(database: ShikiRoomDatabase): MangaDao =
        database.mangaDao()

    @Provides
    fun provideRecentSearchQueriesDao(database: ShikiRoomDatabase): RecentSearchQueriesDao =
        database.recentSearchQueriesDao()

    @Provides
    fun provideUserDao(database: ShikiRoomDatabase): UsersDao =
        database.usersDao()

    @Provides
    fun provideTopicsDao(database: ShikiRoomDatabase): TopicsDao =
        database.topicsDao()

    @Provides
    fun provideUserRateDao(database: ShikiRoomDatabase): UserRateDao =
        database.userRateDao()

    @Provides
    fun provideGenreDao(database: ShikiRoomDatabase): GenreDao =
        database.genreDao()

    @Provides
    fun provideStudioDao(database: ShikiRoomDatabase): StudioDao =
        database.studioDao()

    @Provides
    fun providePublisherDao(database: ShikiRoomDatabase): PublisherDao =
        database.publisherDao()
}