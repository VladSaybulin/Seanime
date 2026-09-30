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
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.database.ANIME_ALIAS
import ru.vladsaybulin.database.ANIME_ROWS
import ru.vladsaybulin.database.CHARACTER_ALIAS
import ru.vladsaybulin.database.CHARACTER_ROWS
import ru.vladsaybulin.database.MANGA_ALIAS
import ru.vladsaybulin.database.MANGA_ROWS
import ru.vladsaybulin.database.PERSON_ALIAS
import ru.vladsaybulin.database.PERSON_ROWS
import ru.vladsaybulin.database.models.anime.AnimeCharacterReferenceWithRoleEntity
import ru.vladsaybulin.database.models.anime.AnimeDetailsEntity
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.anime.AnimeGenreCrossRef
import ru.vladsaybulin.database.models.anime.AnimePersonReferenceWithRolesEntity
import ru.vladsaybulin.database.models.anime.AnimeRelatedEntity
import ru.vladsaybulin.database.models.anime.AnimeScreenshotEntity
import ru.vladsaybulin.database.models.anime.AnimeSimilarAnimeCrossRef
import ru.vladsaybulin.database.models.anime.AnimeStudioCrossRef
import ru.vladsaybulin.database.models.anime.AnimeVideoEntity
import ru.vladsaybulin.database.models.anime.PopulatedAnimeDetails
import ru.vladsaybulin.database.models.title.PopulatedCharacterWithRole
import ru.vladsaybulin.database.models.title.PopulatedPersonWithRoles
import ru.vladsaybulin.database.models.title.PopulatedRelatedTitle

@Dao
interface AnimeDetailsDao {

    // Get methods

    @Query("SELECT * FROM anime_details WHERE id = :animeId")
    @Transaction
    fun getDetailsStream(animeId: Long): Flow<PopulatedAnimeDetails>

    @Query(
        value = """
            SELECT 
                relation_type,
                $ANIME_ROWS,
                $MANGA_ROWS
            FROM (SELECT * FROM anime_related WHERE anime_id = :animeId) AS anime_related
            LEFT OUTER JOIN animes AS $ANIME_ALIAS ON anime_related.related_anime_id = $ANIME_ALIAS.id
            LEFT OUTER JOIN mangas AS $MANGA_ALIAS ON anime_related.related_manga_id = $MANGA_ALIAS.id        
        """
    )
    suspend fun getRelated(animeId: Long): List<PopulatedRelatedTitle>

    @Query(
        value = """
            SELECT
                ac.is_main,
                $CHARACTER_ROWS
            FROM (SELECT character_id, is_main FROM anime_characters WHERE anime_id = :animeId) AS ac
            INNER JOIN characters AS $CHARACTER_ALIAS ON ac.character_id = $CHARACTER_ALIAS.id
        """
    )
    fun getCharactersWithRoleStream(animeId: Long): Flow<List<PopulatedCharacterWithRole>>

    @Query(
        value = """
            SELECT
                apr.roles,
                $PERSON_ROWS
            FROM (SELECT person_id, roles FROM anime_person_roles WHERE anime_id = :animeId) AS apr
            INNER JOIN person AS $PERSON_ALIAS ON apr.person_id = $PERSON_ALIAS.id
        """
    )
    suspend fun getAuthorsWithRoles(animeId: Long): List<PopulatedPersonWithRoles>

    @Query(
        value = """
            SELECT $ANIME_ALIAS.*
            FROM (SELECT similar_id FROM anime_similar_anime WHERE anime_id = :animeId) AS sa
            INNER JOIN animes AS $ANIME_ALIAS ON sa.similar_id = $ANIME_ALIAS.id
        """
    )
    fun getSimilarStream(animeId: Long): Flow<List<AnimeEntity>>


    // Insert methods

    @Insert(onConflict = REPLACE)
    suspend fun insertOrReplaceDetails(details: AnimeDetailsEntity)

    @Insert
    suspend fun insertDetailsReferences(
        relatedTitles: List<AnimeRelatedEntity>,
        genreReferences: List<AnimeGenreCrossRef>,
        studioReferences: List<AnimeStudioCrossRef>,
        screenshots: List<AnimeScreenshotEntity>,
        videos: List<AnimeVideoEntity>
    )

    @Insert
    suspend fun insertRolesReferences(
        characterReferences: List<AnimeCharacterReferenceWithRoleEntity>,
        personReferences: List<AnimePersonReferenceWithRolesEntity>
    )

    @Insert
    suspend fun insertSimilarReferences(
        similarReferences: List<AnimeSimilarAnimeCrossRef>
    )


    // Delete methods

    @Query("DELETE FROM anime_details WHERE id = :animeId")
    suspend fun deleteDetails(animeId: Long)

}