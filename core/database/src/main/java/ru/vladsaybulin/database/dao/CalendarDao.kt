package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.database.models.AnimeDbo
import ru.vladsaybulin.database.models.CalendarItemDbo
import ru.vladsaybulin.database.models.PopulatedCalendarItem

@Dao
interface CalendarDao {

    @Query("SELECT * FROM calendar_items")
    fun getAllCalendarItems(): Flow<PopulatedCalendarItem>

    @Insert
    fun insertCalendarItems(items: List<CalendarItemDbo>)

    @Insert(onConflict = REPLACE)
    fun insertOrReplaceAnime(anime: List<AnimeDbo>)

    @Query("DELETE FROM calendar_items")
    fun deleteAllItems()
}