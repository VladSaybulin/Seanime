package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.vladsaybulin.database.models.filters.FilterGenreEntity
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.genre.GenreKind

@Dao
interface FilterGenreDao {

    @Query("SELECT * FROM filter_genres WHERE id = :genreId")
    suspend fun getFilterGenreById(genreId: Long): FilterGenreEntity?

    @Query("SELECT * FROM filter_genres WHERE entry_type = :entryType AND kind = :genreKind")
    fun getFilterGenresByKind(entryType: EntryType, genreKind: GenreKind): List<FilterGenreEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrIgnoreFilterGenres(genres: List<FilterGenreEntity>)

    @Query("DELETE FROM filter_genres WHERE entry_type = :entryType")
    fun deleteFilterGenresByEntryType(entryType: EntryType)

}