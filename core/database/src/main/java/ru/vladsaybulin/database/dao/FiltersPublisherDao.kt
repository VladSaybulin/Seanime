package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.vladsaybulin.database.models.filters.FilterPublisherEntity

@Dao
interface FiltersPublisherDao {

    @Query("SELECT * FROM filter_publishers WHERE id = :publisherId")
    fun getFilterPublisherById(publisherId: Long): FilterPublisherEntity?

    @Query("SELECT * FROM filter_publishers")
    fun getAllFilterPublishers(): List<FilterPublisherEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrIgnoreFilterPublishers(publishers: List<FilterPublisherEntity>)

    @Query("DELETE FROM filter_publishers")
    fun deleteAllFilterPublishers()

}