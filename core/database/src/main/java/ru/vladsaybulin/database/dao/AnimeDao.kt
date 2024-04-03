package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.vladsaybulin.database.models.AnimeDbo

@Dao
interface AnimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceAnimeEntities(anime: List<AnimeDbo>)
}