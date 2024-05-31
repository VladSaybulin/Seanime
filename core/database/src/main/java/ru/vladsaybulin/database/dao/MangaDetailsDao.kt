package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.database.models.manga.MangaCharacterEntity
import ru.vladsaybulin.database.models.manga.MangaDetailsEntity
import ru.vladsaybulin.database.models.manga.MangaGenreCrossRef
import ru.vladsaybulin.database.models.manga.MangaPersonRolesEntity
import ru.vladsaybulin.database.models.manga.MangaPublisherCrossRef
import ru.vladsaybulin.database.models.manga.MangaRelatedEntity
import ru.vladsaybulin.database.models.manga.PopulatedMangaAuthor
import ru.vladsaybulin.database.models.manga.PopulatedMangaCharacter
import ru.vladsaybulin.database.models.manga.PopulatedMangaDetails
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

    @Query("SELECT * FROM manga_person_roles WHERE manga_id = :mangaId")
    @Transaction
    fun getAllMangaAuthors(mangaId: Long): Flow<List<PopulatedMangaAuthor>>

    @Insert
    suspend fun insertMangaAuthors(authors: List<MangaPersonRolesEntity>)

    @Insert
    suspend fun insertMangaCharacters(characters: List<MangaCharacterEntity>)

    @Insert
    suspend fun insertMangaGenreCrossReferences(animeGenre: List<MangaGenreCrossRef>)

    @Insert
    suspend fun insertMangaRelated(animeRelated: List<MangaRelatedEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplacePublishers(studios: List<PublisherEntity>)

    @Insert
    suspend fun insertMangaPublisherCrossReferences(animeStudioEntities: List<MangaPublisherCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceMangaDetails(animeDetails: MangaDetailsEntity)

    @Query("DELETE FROM manga_person_roles WHERE manga_id = :mangaId")
    suspend fun deleteMangaAuthors(mangaId: Long)

    @Query("DELETE FROM manga_characters WHERE manga_id = :mangaId")
    suspend fun deleteMangaCharacters(mangaId: Long)

    @Query("DELETE FROM manga_genre WHERE manga_id = :mangaId")
    suspend fun deleteMangaGenreCrossReferences(mangaId: Long)

    @Query("DELETE FROM manga_related WHERE manga_id = :mangaId")
    suspend fun deleteMangaRelated(mangaId: Long)

    @Query("DELETE FROM manga_publisher WHERE manga_id = :mangaId")
    suspend fun deleteMangaPublisherCrossReferences(mangaId: Long)
    
}