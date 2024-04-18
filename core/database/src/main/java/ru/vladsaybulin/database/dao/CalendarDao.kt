package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.database.models.calendar.CalendarItemEntity
import ru.vladsaybulin.database.models.calendar.PopulatedCalendarItem

@Dao
interface CalendarDao {

    @Transaction
    @Query("SELECT * FROM calendar_items")
    fun getAllCalendarItems(): Flow<List<PopulatedCalendarItem>>

    @Transaction
    @Query(
        value = """
            SELECT calendar_items.* FROM calendar_items 
            JOIN animes ON anime_id = animes.id 
            WHERE name LIKE '%' | :searchQuery | '%' OR russian_name LIKE '%' | :searchQuery | '%'
        """
    )
    suspend fun searchCalendarItems(searchQuery: String): List<PopulatedCalendarItem>

    @Insert
    fun insertCalendarItems(items: List<CalendarItemEntity>)

    @Query("DELETE FROM calendar_items")
    fun deleteAllItems()
}