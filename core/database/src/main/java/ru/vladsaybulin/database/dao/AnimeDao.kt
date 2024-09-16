package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import ru.vladsaybulin.database.models.anime.AnimeEntity

@Dao
interface AnimeDao {

    @Query("SELECT * FROM animes WHERE id = :animeId")
    suspend fun getAnimeById(animeId: Long): AnimeEntity

    @Upsert
    suspend fun upsertAnime(anime: AnimeEntity)

    @Upsert
    suspend fun upsertAnimes(animes: List<AnimeEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreAnimes(animes: List<AnimeEntity>)
}