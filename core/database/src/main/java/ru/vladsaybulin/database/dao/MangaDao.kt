package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.vladsaybulin.database.models.MangaDbo

@Dao
interface MangaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceMangaEntity(manga: MangaDbo)

}