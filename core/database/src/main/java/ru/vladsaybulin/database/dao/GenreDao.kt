package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.vladsaybulin.database.models.genre.GenreEntity
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.genre.GenreKind

@Dao
interface GenreDao {

    @Query("SELECT * FROM genres WHERE entry_type = :entryType AND kind = :genreKind")
    fun getGenresByKind(entryType: EntryType, genreKind: GenreKind): List<GenreEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrIgnoreGenre(studioEntity: GenreEntity)

    @Insert
    fun insertAllGenres(studios: List<GenreEntity>)

    @Query("DELETE FROM genres WHERE entry_type = :entryType")
    fun deleteGenresByEntryType(entryType: EntryType)

}