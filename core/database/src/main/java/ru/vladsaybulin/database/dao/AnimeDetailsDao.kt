package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.database.models.anime.AnimeCharacterEntity
import ru.vladsaybulin.database.models.anime.AnimeDetailsEntity
import ru.vladsaybulin.database.models.anime.AnimeGenreCrossRef
import ru.vladsaybulin.database.models.anime.AnimePersonRolesEntity
import ru.vladsaybulin.database.models.anime.AnimeRelatedEntity
import ru.vladsaybulin.database.models.anime.AnimeScreenshotEntity
import ru.vladsaybulin.database.models.anime.AnimeStudioCrossRef
import ru.vladsaybulin.database.models.anime.AnimeVideoEntity
import ru.vladsaybulin.database.models.anime.PopulatedAnimeAuthor
import ru.vladsaybulin.database.models.anime.PopulatedAnimeCharacter
import ru.vladsaybulin.database.models.anime.PopulatedAnimeDetails
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

    @Query("SELECT * FROM anime_person_roles WHERE anime_id = :animeId")
    @Transaction
    fun getAllAnimeAuthors(animeId: Long): Flow<List<PopulatedAnimeAuthor>>

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceStudios(studios: List<StudioEntity>)

    @Insert
    suspend fun insertAnimeStudioCrossReferences(animeStudioEntities: List<AnimeStudioCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceAnimeDetails(animeDetails: AnimeDetailsEntity)

}