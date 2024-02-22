package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.database.models.AnimeDbo
import ru.vladsaybulin.database.models.CalendarItemDbo
import ru.vladsaybulin.database.models.PopulatedCalendarItem

@Dao
interface CalendarDao {

    @Transaction
    @Query("SELECT * FROM calendar_items")
    fun getAllCalendarItems(): Flow<List<PopulatedCalendarItem>>

    @Transaction
    @Query(
        value = """
            SELECT * FROM calendar_items 
            JOIN animes ON anime_id = animes.id 
            WHERE name LIKE '%' | :searchQuery | '%' OR russian_name LIKE '%' | :searchQuery | '%'
        """
    )
    suspend fun searchCalendarItems(searchQuery: String): List<PopulatedCalendarItem>

    @Insert
    fun insertCalendarItems(items: List<CalendarItemDbo>)

    @Insert(onConflict = REPLACE)
    fun insertOrReplaceAnimeEntities(anime: List<AnimeDbo>)

    @Query("DELETE FROM calendar_items")
    fun deleteAllItems()
}