package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.vladsaybulin.database.models.filters.FilterStudioEntity

@Dao
interface FilterStudioDao {

    @Query("SELECT * FROM filter_studios WHERE id = :studioId")
    fun getFilterStudioById(studioId: Long): FilterStudioEntity?

    @Query("SELECT * FROM filter_studios")
    fun getAllFilterStudios(): List<FilterStudioEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrIgnoreFilterStudio(studioEntity: FilterStudioEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrIgnoreFilterStudios(studios: List<FilterStudioEntity>)

    @Query("DELETE FROM filter_studios")
    fun deleteAllFilterStudios()

}