/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vladsaybulin.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.vladsaybulin.database.dao.AnimeDao
import ru.vladsaybulin.database.dao.AnimeDetailsDao
import ru.vladsaybulin.database.dao.CalendarDao
import ru.vladsaybulin.database.dao.CharacterDao
import ru.vladsaybulin.database.dao.FilterGenreDao
import ru.vladsaybulin.database.dao.FilterStudioDao
import ru.vladsaybulin.database.dao.FiltersPublisherDao
import ru.vladsaybulin.database.dao.GenreDao
import ru.vladsaybulin.database.dao.LastRequestDao
import ru.vladsaybulin.database.dao.MangaDao
import ru.vladsaybulin.database.dao.MangaDetailsDao
import ru.vladsaybulin.database.dao.OngoingAnimeDao
import ru.vladsaybulin.database.dao.PersonDao
import ru.vladsaybulin.database.dao.TopicsDao
import ru.vladsaybulin.database.dao.UserRateDao
import ru.vladsaybulin.database.dao.UsersDao
import ru.vladsaybulin.database.models.anime.AnimeCharacterEntity
import ru.vladsaybulin.database.models.anime.AnimeDetailsEntity
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.anime.AnimeGenreCrossRef
import ru.vladsaybulin.database.models.anime.AnimePersonRolesEntity
import ru.vladsaybulin.database.models.anime.AnimeRelatedEntity
import ru.vladsaybulin.database.models.anime.AnimeScreenshotEntity
import ru.vladsaybulin.database.models.anime.AnimeSimilarAnimeCrossRef
import ru.vladsaybulin.database.models.anime.AnimeStudioCrossRef
import ru.vladsaybulin.database.models.anime.AnimeVideoEntity
import ru.vladsaybulin.database.models.anime.OngoingAnimeEntity
import ru.vladsaybulin.database.models.anime.StudioEntity
import ru.vladsaybulin.database.models.calendar.CalendarItemEntity
import ru.vladsaybulin.database.models.character.CharacterAnimeCrossRef
import ru.vladsaybulin.database.models.character.CharacterDetailsEntity
import ru.vladsaybulin.database.models.character.CharacterEntity
import ru.vladsaybulin.database.models.character.CharacterMangaCrossRef
import ru.vladsaybulin.database.models.character.CharacterSeyuCrossRef
import ru.vladsaybulin.database.models.filters.FilterGenreEntity
import ru.vladsaybulin.database.models.filters.FilterPublisherEntity
import ru.vladsaybulin.database.models.filters.FilterStudioEntity
import ru.vladsaybulin.database.models.genre.GenreEntity
import ru.vladsaybulin.database.models.lastrequest.LastRequestEntity
import ru.vladsaybulin.database.models.manga.MangaCharacterEntity
import ru.vladsaybulin.database.models.manga.MangaDetailsEntity
import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.database.models.manga.MangaGenreCrossRef
import ru.vladsaybulin.database.models.manga.MangaPersonRolesEntity
import ru.vladsaybulin.database.models.manga.MangaPublisherCrossRef
import ru.vladsaybulin.database.models.manga.MangaRelatedEntity
import ru.vladsaybulin.database.models.manga.MangaSimilarMangaCrossRef
import ru.vladsaybulin.database.models.manga.PublisherEntity
import ru.vladsaybulin.database.models.person.PersonEntity
import ru.vladsaybulin.database.models.topic.TopicEntity
import ru.vladsaybulin.database.models.user.UserEntity
import ru.vladsaybulin.database.models.userrate.InProgressUserRateEntity
import ru.vladsaybulin.database.models.userrate.PagedUserRateEntity
import ru.vladsaybulin.database.models.userrate.UserRateEntity
import ru.vladsaybulin.database.utils.AnimeKindTypeConverter
import ru.vladsaybulin.database.utils.AnimeRatingTypeConverter
import ru.vladsaybulin.database.utils.EntryStatusTypeConverter
import ru.vladsaybulin.database.utils.GenreKindTypeConverter
import ru.vladsaybulin.database.utils.InstantTypeConverter
import ru.vladsaybulin.database.utils.IntStatisticsItemsConverter
import ru.vladsaybulin.database.utils.LastRequestTypeTypeConverter
import ru.vladsaybulin.database.utils.MangaKindTypeConverter
import ru.vladsaybulin.database.utils.StatsTypeConverter
import ru.vladsaybulin.database.utils.StatusStatisticsItemsConverter
import ru.vladsaybulin.database.utils.StringListTypeConverter
import ru.vladsaybulin.database.utils.TextRangesTypeConverter
import ru.vladsaybulin.database.utils.TopicEventTypeConverter
import ru.vladsaybulin.database.utils.TopicLinkedTypeTypeConverter
import ru.vladsaybulin.database.utils.TopicTypeTypeConverter
import ru.vladsaybulin.database.utils.UserRateOrderFieldTypeConverter
import ru.vladsaybulin.database.utils.UserRateOrderTypeConverter
import ru.vladsaybulin.database.utils.UserRateTypeConverter
import ru.vladsaybulin.database.utils.VideoKindTypeConverter

@Database(
    entities = [
        /* Base entities */
        AnimeEntity::class,
        MangaEntity::class,
        PersonEntity::class,
        CharacterEntity::class,
        UserRateEntity::class,
        UserEntity::class,
        TopicEntity::class,

        /* Search entities */
        FilterStudioEntity::class,
        FilterPublisherEntity::class,
        FilterGenreEntity::class,

        /* Details common entities */
        GenreEntity::class,
        StudioEntity::class,
        PublisherEntity::class,

        /* Anime details entities */
        AnimeDetailsEntity::class,
        AnimeGenreCrossRef::class,
        AnimePersonRolesEntity::class,
        AnimeCharacterEntity::class,
        AnimeRelatedEntity::class,
        AnimeStudioCrossRef::class,
        AnimeScreenshotEntity::class,
        AnimeVideoEntity::class,
        AnimeSimilarAnimeCrossRef::class,

        /* Manga details entities */
        MangaDetailsEntity::class,
        MangaGenreCrossRef::class,
        MangaPersonRolesEntity::class,
        MangaCharacterEntity::class,
        MangaRelatedEntity::class,
        MangaPublisherCrossRef::class,
        MangaSimilarMangaCrossRef::class,

        /* Character details */
        CharacterDetailsEntity::class,
        CharacterSeyuCrossRef::class,
        CharacterAnimeCrossRef::class,
        CharacterMangaCrossRef::class,

        LastRequestEntity::class,

        /* Other entities */
        CalendarItemEntity::class,
        OngoingAnimeEntity::class,
        PagedUserRateEntity::class,
        InProgressUserRateEntity::class
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
        GenreKindTypeConverter::class,
        StringListTypeConverter::class,
        AnimeRatingTypeConverter::class,
        IntStatisticsItemsConverter::class,
        StatusStatisticsItemsConverter::class,
        VideoKindTypeConverter::class,
        TextRangesTypeConverter::class,
        LastRequestTypeTypeConverter::class,
        UserRateOrderTypeConverter::class,
        UserRateOrderFieldTypeConverter::class,
        StatsTypeConverter::class
    ]
)
abstract class SeanimeRoomDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao
    abstract fun mangaDao(): MangaDao
    abstract fun characterDao(): CharacterDao
    abstract fun personDao(): PersonDao
    abstract fun userRateDao(): UserRateDao
    abstract fun usersDao(): UsersDao
    abstract fun topicsDao(): TopicsDao

    abstract fun filterStudioDao(): FilterStudioDao
    abstract fun filterPublisherDao(): FiltersPublisherDao
    abstract fun filterGenreDao(): FilterGenreDao

    abstract fun genreDao(): GenreDao
    abstract fun animeDetailsDao(): AnimeDetailsDao
    abstract fun mangaDetailsDao(): MangaDetailsDao
    abstract fun lastRequestDao(): LastRequestDao

    abstract fun calendarDao(): CalendarDao

    abstract fun ongoingAnimeDao(): OngoingAnimeDao

}