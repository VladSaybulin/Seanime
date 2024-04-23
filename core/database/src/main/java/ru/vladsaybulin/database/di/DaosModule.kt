package ru.vladsaybulin.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.vladsaybulin.database.ShikiRoomDatabase
import ru.vladsaybulin.database.dao.AnimeDao
import ru.vladsaybulin.database.dao.AnimeDetailsDao
import ru.vladsaybulin.database.dao.CalendarDao
import ru.vladsaybulin.database.dao.CharacterDao
import ru.vladsaybulin.database.dao.FilterStudioDao
import ru.vladsaybulin.database.dao.FiltersPublisherDao
import ru.vladsaybulin.database.dao.LastRequestDao
import ru.vladsaybulin.database.dao.MangaDao
import ru.vladsaybulin.database.dao.MangaDetailsDao
import ru.vladsaybulin.database.dao.OngoingAnimeDao
import ru.vladsaybulin.database.dao.PersonDao
import ru.vladsaybulin.database.dao.RecentSearchQueriesDao
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
    fun provideMangaDao(database: ShikiRoomDatabase): MangaDao =
        database.mangaDao()

    @Provides
    fun providePersonDao(database: ShikiRoomDatabase): PersonDao =
        database.personDao()

    @Provides
    fun provideCharactersDao(database: ShikiRoomDatabase): CharacterDao =
        database.characterDao()

    @Provides
    fun provideUserRateDao(database: ShikiRoomDatabase): UserRateDao =
        database.userRateDao()

    @Provides
    fun provideUserDao(database: ShikiRoomDatabase): UsersDao =
        database.usersDao()

    @Provides
    fun provideTopicsDao(database: ShikiRoomDatabase): TopicsDao =
        database.topicsDao()

    @Provides
    fun provideRecentSearchQueriesDao(database: ShikiRoomDatabase): RecentSearchQueriesDao =
        database.recentSearchQueriesDao()

    @Provides
    fun provideFilterStudioDao(database: ShikiRoomDatabase): FilterStudioDao =
        database.filterStudioDao()

    @Provides
    fun provideFilterPublisherDao(database: ShikiRoomDatabase): FiltersPublisherDao =
        database.filterPublisherDao()

    @Provides
    fun provideFilterGenreDao(database: ShikiRoomDatabase) =
        database.filterGenreDao()

    @Provides
    fun provideGenreDao(database: ShikiRoomDatabase) =
        database.genreDao()

    @Provides
    fun provideAnimeDetailsDao(database: ShikiRoomDatabase): AnimeDetailsDao =
        database.animeDetailsDao()

    @Provides
    fun provideMangaDetailsDao(database: ShikiRoomDatabase): MangaDetailsDao =
        database.mangaDetailsDao()

    @Provides
    fun provideLastRequestDao(database: ShikiRoomDatabase): LastRequestDao =
        database.lastRequestDao()

    @Provides
    fun provideCalendarDao(database: ShikiRoomDatabase): CalendarDao =
        database.calendarDao()

    @Provides
    fun provideOngoingAnimesDao(database: ShikiRoomDatabase): OngoingAnimeDao =
        database.ongoingAnimeDao()
}