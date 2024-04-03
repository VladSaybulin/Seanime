package ru.vladsaybulin.database.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.vladsaybulin.database.models.AnimeDbo

interface AnimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceAnimeEntities(anime: List<AnimeDbo>)
}