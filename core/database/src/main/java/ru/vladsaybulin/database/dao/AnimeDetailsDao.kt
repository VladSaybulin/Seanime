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

package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.database.models.anime.AnimeCharacterEntity
import ru.vladsaybulin.database.models.anime.AnimeDetailsEntity
import ru.vladsaybulin.database.models.anime.AnimeGenreCrossRef
import ru.vladsaybulin.database.models.anime.AnimePersonRolesEntity
import ru.vladsaybulin.database.models.anime.AnimeRelatedEntity
import ru.vladsaybulin.database.models.anime.AnimeScreenshotEntity
import ru.vladsaybulin.database.models.anime.AnimeSimilarAnimeCrossRef
import ru.vladsaybulin.database.models.anime.AnimeStudioCrossRef
import ru.vladsaybulin.database.models.anime.AnimeVideoEntity
import ru.vladsaybulin.database.models.anime.PopulatedAnimeAuthor
import ru.vladsaybulin.database.models.anime.PopulatedAnimeCharacter
import ru.vladsaybulin.database.models.anime.PopulatedAnimeDetails
import ru.vladsaybulin.database.models.anime.PopulatedAnimeRelated
import ru.vladsaybulin.database.models.anime.PopulatedSimilarAnime
import ru.vladsaybulin.database.models.anime.StudioEntity

@Dao
interface AnimeDetailsDao {

    @Query("SELECT * FROM anime_details WHERE id = :animeId")
    @Transaction
    fun getAnimeDetails(animeId: Long): Flow<PopulatedAnimeDetails>

    @Query("SELECT * FROM anime_characters WHERE anime_id = :animeId AND is_main = 1")
    @Transaction
    fun getMainAnimeCharacters(animeId: Long): Flow<List<PopulatedAnimeCharacter>>

    @Query("SELECT * FROM anime_person_roles WHERE anime_id = :animeId AND is_main = 1")
    @Transaction
    fun getMainAnimeAuthors(animeId: Long): Flow<List<PopulatedAnimeAuthor>>

    @Query("SELECT * FROM anime_related WHERE anime_id = :animeId LIMIT :limit")
    @Transaction
    fun getFirstAnimeRelated(animeId: Long, limit: Int): Flow<List<PopulatedAnimeRelated>>

    @Query("SELECT * FROM anime_screenshots WHERE anime_id = :animeId")
    fun getAnimeScreenshots(animeId: Long): Flow<List<AnimeScreenshotEntity>>

    @Query("SELECT * FROM anime_videos WHERE anime_id = :animeId LIMIT :limit")
    fun getFirstAnimeVideos(animeId: Long, limit: Int): Flow<List<AnimeVideoEntity>>

    @Query("SELECT * FROM anime_similar_anime WHERE anime_id = :animeId")
    @Transaction
    fun getSimilarAnimes(animeId: Long): Flow<List<PopulatedSimilarAnime>>

    @Query("SELECT * FROM anime_person_roles WHERE anime_id = :animeId ORDER BY is_main")
    @Transaction
    fun getAllAnimeAuthors(animeId: Long): Flow<List<PopulatedAnimeAuthor>>

    @Query("SELECT * FROM anime_related WHERE anime_id = :animeId")
    @Transaction
    fun getAllAnimeRelatedTitles(animeId: Long): Flow<List<PopulatedAnimeRelated>>

    @Query("SELECT * FROM anime_characters WHERE anime_id = :animeId ORDER BY is_main DESC")
    fun getAllAnimeCharacters(animeId: Long): Flow<List<PopulatedAnimeCharacter>>

    @Query("SELECT * FROM anime_videos WHERE anime_id = :animeId")
    fun getAllAnimeVideos(animeId: Long): Flow<List<AnimeVideoEntity>>

    @Insert
    suspend fun insertAnimeAuthors(authors: List<AnimePersonRolesEntity>)

    @Insert
    suspend fun insertAnimeCharacters(characters: List<AnimeCharacterEntity>)

    @Insert
    suspend fun insertAnimeGenreCrossReferences(animeGenre: List<AnimeGenreCrossRef>)

    @Insert
    suspend fun insertAnimeScreenshots(animeScreenshots: List<AnimeScreenshotEntity>)

    @Insert
    suspend fun insertAnimeVideos(animeVideos: List<AnimeVideoEntity>)

    @Insert
    suspend fun insertAnimeRelated(animeRelated: List<AnimeRelatedEntity>)

    @Insert
    suspend fun insertAnimeSimilarAnimeCrossReferences(animeSimilarAnimes: List<AnimeSimilarAnimeCrossRef>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreStudios(studios: List<StudioEntity>)

    @Insert
    suspend fun insertAnimeStudioCrossReferences(animeStudioEntities: List<AnimeStudioCrossRef>)

    @Upsert
    suspend fun upsertAnimeDetails(animeDetails: AnimeDetailsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceAnimeDetails(animeDetails: AnimeDetailsEntity)

    @Query("DELETE FROM anime_similar_anime WHERE anime_id = :animeId")
    suspend fun deleteAnimeSimilarAnimeCrossRef(animeId: Long)

    @Query("DELETE FROM anime_characters WHERE anime_id = :animeId")
    suspend fun deleteAnimeCharacters(animeId: Long)

    @Query("DELETE FROM anime_person_roles WHERE anime_id = :animeId")
    suspend fun deleteAnimePersonRoles(animeId: Long)

    @Query("DELETE FROM anime_genre WHERE anime_id = :animeId")
    suspend fun deleteAnimeGenreCrossReferences(animeId: Long)

    @Query("DELETE FROM anime_studio WHERE anime_id = :animeId")
    suspend fun deleteAnimeStudioCrossReferences(animeId: Long)

    @Query("DELETE FROM anime_related WHERE anime_id = :animeId")
    suspend fun deleteAnimeRelated(animeId: Long)

    @Query("DELETE FROM anime_screenshots WHERE anime_id = :animeId")
    suspend fun deleteAnimeScreenshots(animeId: Long)

    @Query("DELETE FROM anime_videos WHERE anime_id = :animeId")
    suspend fun deleteAnimeVideos(animeId: Long)

}