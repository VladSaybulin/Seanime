package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.vladsaybulin.database.models.anime.StudioEntity

@Dao
interface StudioDao {

    @Query("SELECT * FROM studios")
    fun getAllStudios(): List<StudioEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrIgnoreStudio(studioEntity: StudioEntity)

    @Insert
    fun insertAllStudios(studios: List<StudioEntity>)

    @Query("DELETE FROM studios")
    fun deleteAllStudios()

}