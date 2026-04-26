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
import ru.vladsaybulin.database.models.manga.MangaCharacterEntity
import ru.vladsaybulin.database.models.manga.MangaDetailsEntity
import ru.vladsaybulin.database.models.manga.MangaGenreCrossRef
import ru.vladsaybulin.database.models.manga.MangaPersonRolesEntity
import ru.vladsaybulin.database.models.manga.MangaPublisherCrossRef
import ru.vladsaybulin.database.models.manga.MangaRelatedEntity
import ru.vladsaybulin.database.models.manga.MangaSimilarMangaCrossRef
import ru.vladsaybulin.database.models.manga.PopulatedMangaAuthor
import ru.vladsaybulin.database.models.manga.PopulatedMangaCharacter
import ru.vladsaybulin.database.models.manga.PopulatedMangaDetails
import ru.vladsaybulin.database.models.manga.PopulatedMangaRelated
import ru.vladsaybulin.database.models.manga.PopulatedSimilarManga
import ru.vladsaybulin.database.models.manga.PublisherEntity

@Dao
interface MangaDetailsDao {

    @Query("SELECT * FROM manga_details WHERE id = :mangaId")
    @Transaction
    fun getMangaDetails(mangaId: Long): Flow<PopulatedMangaDetails>

    @Query("SELECT * FROM manga_characters WHERE manga_id = :mangaId AND is_main = 1")
    @Transaction
    fun getMainMangaCharacters(mangaId: Long): Flow<List<PopulatedMangaCharacter>>

    @Query("SELECT * FROM manga_person_roles WHERE manga_id = :mangaId AND is_main = 1")
    @Transaction
    fun getMainMangaAuthors(mangaId: Long): Flow<List<PopulatedMangaAuthor>>

    @Query("SELECT * FROM manga_related WHERE manga_id = :mangaId LIMIT :limit")
    @Transaction
    fun getFirstMangaRelated(mangaId: Long, limit: Int): Flow<List<PopulatedMangaRelated>>

    @Query("SELECT * FROM manga_similar_manga WHERE manga_id = :mangaId")
    @Transaction
    fun getSimilarMangas(mangaId: Long): Flow<List<PopulatedSimilarManga>>

    @Query("SELECT * FROM manga_person_roles WHERE manga_id = :mangaId")
    @Transaction
    fun getAllMangaAuthors(mangaId: Long): Flow<List<PopulatedMangaAuthor>>

    @Query("SELECT * FROM manga_related WHERE manga_id = :mangaId")
    @Transaction
    fun getAllMangaRelatedTitles(mangaId: Long): Flow<List<PopulatedMangaRelated>>

    @Query("SELECT * FROM manga_characters WHERE manga_id = :mangaId ORDER BY is_main DESC")
    fun getAllMangaCharacters(mangaId: Long): Flow<List<PopulatedMangaCharacter>>

    @Insert
    suspend fun insertMangaAuthors(authors: List<MangaPersonRolesEntity>)

    @Insert
    suspend fun insertMangaCharacters(characters: List<MangaCharacterEntity>)

    @Insert
    suspend fun insertMangaGenreCrossReferences(animeGenre: List<MangaGenreCrossRef>)

    @Insert
    suspend fun insertMangaRelated(animeRelated: List<MangaRelatedEntity>)

    @Insert
    suspend fun insertMangaSimilarMangaCrossReferences(mangaSimilarAnimes: List<MangaSimilarMangaCrossRef>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnorePublishers(studios: List<PublisherEntity>)

    @Insert
    suspend fun insertMangaPublisherCrossReferences(animeStudioEntities: List<MangaPublisherCrossRef>)

    @Upsert
    suspend fun upsertMangaDetails(mangaDetails: MangaDetailsEntity)

    @Query("DELETE FROM manga_similar_manga WHERE manga_id = :mangaId")
    suspend fun deleteMangaSimilarMangaCrossRef(mangaId: Long)

    @Query("DELETE FROM manga_characters WHERE manga_id= :mangaId")
    suspend fun deleteMangaCharacters(mangaId: Long)

    @Query("DELETE FROM manga_person_roles WHERE manga_id = :mangaId")
    suspend fun deleteMangaPersonRoles(mangaId: Long)

    @Query("DELETE FROM manga_genre WHERE manga_id = :mangaId")
    suspend fun deleteMangaGenreCrossReferences(mangaId: Long)

    @Query("DELETE FROM manga_publisher WHERE manga_id = :mangaId")
    suspend fun deleteMangaPublisherCrossReferences(mangaId: Long)

    @Query("DELETE FROM manga_related WHERE manga_id = :mangaId")
    suspend fun deleteMangaRelated(mangaId: Long)

}