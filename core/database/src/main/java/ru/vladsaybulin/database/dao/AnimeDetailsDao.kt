package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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
import ru.vladsaybulin.database.models.anime.PopulatedAnimeDetails
import ru.vladsaybulin.database.models.anime.StudioEntity

@Dao
interface AnimeDetailsDao {

    @Query("SELECT * FROM anime_details WHERE id = :animeId")
    fun getAnimeDetails(animeId: Long): Flow<PopulatedAnimeDetails>

    @Query("SELECT * FROM anime_person_roles WHERE anime_id = :animeId")
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

    @Query("DELETE FROM anime_person_roles WHERE anime_id = :animeId")
    suspend fun deleteAnimeAuthors(animeId: Long)

    @Query("DELETE FROM anime_characters WHERE anime_id = :animeId")
    suspend fun deleteAnimeCharacters(animeId: Long)

    @Query("DELETE FROM anime_genre WHERE anime_id = :animeId")
    suspend fun deleteAnimeGenreCrossReferences(animeId: Long)

    @Query("DELETE FROM anime_screenshots WHERE anime_id = :animeId")
    suspend fun deleteAnimeScreenshots(animeId: Long)

    @Query("DELETE FROM anime_videos WHERE anime_id = :animeId")
    suspend fun deleteAnimeVideos(animeId: Long)

    @Query("DELETE FROM anime_related WHERE anime_id = :animeId")
    suspend fun deleteAnimeRelated(animeId: Long)

    @Query("DELETE FROM anime_studio WHERE  anime_id = :animeId")
    suspend fun deleteAnimeStudioCrossReferences(animeId: Long)

}