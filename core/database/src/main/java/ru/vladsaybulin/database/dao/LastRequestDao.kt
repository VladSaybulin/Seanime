package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.datetime.Instant
import ru.vladsaybulin.database.models.lastrequest.LastAnimeDetailsRequestEntity
import ru.vladsaybulin.database.models.lastrequest.LastCharacterDetailsRequestEntity
import ru.vladsaybulin.database.models.lastrequest.LastMangaDetailsRequestEntity

@Dao
interface LastRequestDao {

    @Query("SELECT request_date FROM last_anime_request WHERE anime_id = :animeId")
    suspend fun getLastAnimeDetailsRequestDate(animeId: Long): Instant?

    @Query("SELECT request_date FROM last_character_request WHERE character_id = :characterId")
    suspend fun getLastCharacterDetailsRequestDate(characterId: Long): Instant?

    @Query("SELECT request_date FROM last_manga_request WHERE manga_id = :mangaId")
    suspend fun getLastMangaDetailsRequestDate(mangaId: Long): Instant?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceLastAnimeDetailsRequest(lastAnimeDetailsRequestEntity: LastAnimeDetailsRequestEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceLastMangaDetailsRequest(lastMangaDetailsRequestEntity: LastMangaDetailsRequestEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceLastCharacterDetailsRequest(lastCharacterDetailsRequestEntity: LastCharacterDetailsRequestEntity)

}