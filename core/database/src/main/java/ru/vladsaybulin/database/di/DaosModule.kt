package ru.vladsaybulin.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.vladsaybulin.database.SeanimeRoomDatabase
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
import ru.vladsaybulin.database.dao.TopicsDao
import ru.vladsaybulin.database.dao.UserRateDao
import ru.vladsaybulin.database.dao.UsersDao

@Module
@InstallIn(SingletonComponent::class)
class DaosModule {

    @Provides
    fun provideAnimeDao(database: SeanimeRoomDatabase): AnimeDao =
        database.animeDao()

    @Provides
    fun provideMangaDao(database: SeanimeRoomDatabase): MangaDao =
        database.mangaDao()

    @Provides
    fun providePersonDao(database: SeanimeRoomDatabase): PersonDao =
        database.personDao()

    @Provides
    fun provideCharactersDao(database: SeanimeRoomDatabase): CharacterDao =
        database.characterDao()

    @Provides
    fun provideUserRateDao(database: SeanimeRoomDatabase): UserRateDao =
        database.userRateDao()

    @Provides
    fun provideUserDao(database: SeanimeRoomDatabase): UsersDao =
        database.usersDao()

    @Provides
    fun provideTopicsDao(database: SeanimeRoomDatabase): TopicsDao =
        database.topicsDao()

    @Provides
    fun provideFilterStudioDao(database: SeanimeRoomDatabase): FilterStudioDao =
        database.filterStudioDao()

    @Provides
    fun provideFilterPublisherDao(database: SeanimeRoomDatabase): FiltersPublisherDao =
        database.filterPublisherDao()

    @Provides
    fun provideFilterGenreDao(database: SeanimeRoomDatabase) =
        database.filterGenreDao()

    @Provides
    fun provideGenreDao(database: SeanimeRoomDatabase) =
        database.genreDao()

    @Provides
    fun provideAnimeDetailsDao(database: SeanimeRoomDatabase): AnimeDetailsDao =
        database.animeDetailsDao()

    @Provides
    fun provideMangaDetailsDao(database: SeanimeRoomDatabase): MangaDetailsDao =
        database.mangaDetailsDao()

    @Provides
    fun provideLastRequestDao(database: SeanimeRoomDatabase): LastRequestDao =
        database.lastRequestDao()

    @Provides
    fun provideCalendarDao(database: SeanimeRoomDatabase): CalendarDao =
        database.calendarDao()

    @Provides
    fun provideOngoingAnimesDao(database: SeanimeRoomDatabase): OngoingAnimeDao =
        database.ongoingAnimeDao()
}