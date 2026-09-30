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
import ru.vladsaybulin.database.models.manga.MangaCharacterReferenceWithRoleEntity
import ru.vladsaybulin.database.models.manga.MangaDetailsEntity
import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.database.models.manga.MangaGenreCrossRef
import ru.vladsaybulin.database.models.manga.MangaPersonReferenceWithRolesEntity
import ru.vladsaybulin.database.models.manga.MangaPublisherCrossRef
import ru.vladsaybulin.database.models.manga.MangaRelatedEntity
import ru.vladsaybulin.database.models.manga.MangaSimilarMangaCrossRef
import ru.vladsaybulin.database.models.manga.PopulatedMangaDetails
import ru.vladsaybulin.database.models.title.PopulatedCharacterWithRole
import ru.vladsaybulin.database.models.title.PopulatedPersonWithRoles
import ru.vladsaybulin.database.models.title.PopulatedRelatedTitle


@Dao
interface MangaDetailsDao {

    // Get methods

    @Query("SELECT * FROM anime_details WHERE id = :mangaId")
    @Transaction
    fun getDetailsStream(mangaId: Long): Flow<PopulatedMangaDetails>

    @Query(
        value = """
            SELECT 
                relation_type,
                $ANIME_ROWS,
                $MANGA_ROWS
            FROM (SELECT * FROM manga_related WHERE manga_id = :mangaId) AS anime_related
            LEFT OUTER JOIN animes AS $ANIME_ALIAS ON anime_related.related_anime_id = $ANIME_ALIAS.id
            LEFT OUTER JOIN mangas AS $MANGA_ALIAS ON anime_related.related_manga_id = $MANGA_ALIAS.id        
        """
    )
    suspend fun getRelated(mangaId: Long): List<PopulatedRelatedTitle>

    @Query(
        value = """
            SELECT
                ac.is_main,
                $CHARACTER_ROWS
            FROM (SELECT character_id, is_main FROM manga_characters WHERE manga_id = :mangaId) AS ac
            INNER JOIN characters AS $CHARACTER_ALIAS ON ac.character_id = $CHARACTER_ALIAS.id
        """
    )
    fun getCharactersWithRoleStream(mangaId: Long): Flow<List<PopulatedCharacterWithRole>>

    @Query(
        value = """
            SELECT
                apr.roles,
                $PERSON_ROWS
            FROM (SELECT person_id, roles FROM manga_person_roles WHERE manga_id = :mangaId) AS apr
            INNER JOIN person AS $PERSON_ALIAS ON apr.person_id = $PERSON_ALIAS.id
        """
    )
    suspend fun getAuthorsWithRoles(mangaId: Long): List<PopulatedPersonWithRoles>

    @Query(
        value = """
            SELECT $MANGA_ALIAS.*
            FROM (SELECT similar_id FROM manga_similar_manga WHERE manga_id = :mangaId) AS sa
            INNER JOIN mangas AS $MANGA_ALIAS ON sa.similar_id = $MANGA_ALIAS.id
        """
    )
    fun getSimilarStream(mangaId: Long): Flow<List<MangaEntity>>


    // Insert methods

    @Insert(onConflict = REPLACE)
    suspend fun insertOrReplaceDetails(details: MangaDetailsEntity)

    @Insert
    suspend fun insertDetailsReferences(
        relatedTitles: List<MangaRelatedEntity>,
        genreReferences: List<MangaGenreCrossRef>,
        publisherReferences: List<MangaPublisherCrossRef>
    )

    @Insert
    suspend fun insertRolesReferences(
        characterReferences: List<MangaCharacterReferenceWithRoleEntity>,
        personReferences: List<MangaPersonReferenceWithRolesEntity>
    )

    @Insert
    suspend fun insertSimilarReferences(
        similarReferences: List<MangaSimilarMangaCrossRef>
    )


    // Delete methods

    @Query("DELETE FROM anime_details WHERE id = :mangaId")
    suspend fun deleteDetails(mangaId: Long)

}