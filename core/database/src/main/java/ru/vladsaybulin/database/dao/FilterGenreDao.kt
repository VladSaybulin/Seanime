/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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