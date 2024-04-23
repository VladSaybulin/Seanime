package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.vladsaybulin.database.models.anime.AnimeEntity

@Dao
interface AnimeDao {

    @Query("SELECT * FROM animes WHERE id = :animeId")
    suspend fun getAnimeById(animeId: Long): AnimeEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceAnime(anime: AnimeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceAnimes(anime: List<AnimeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceAnimeEntity(anime: AnimeEntity)
}