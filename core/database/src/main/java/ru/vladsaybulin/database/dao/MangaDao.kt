package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import ru.vladsaybulin.database.models.manga.MangaEntity

@Dao
interface MangaDao {

    @Query("SELECT * FROM mangas WHERE id = :mangaId")
    suspend fun getMangaById(mangaId: Long): MangaEntity

    @Upsert
    suspend fun upsertManga(manga: MangaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceMangas(mangas: List<MangaEntity>)
}