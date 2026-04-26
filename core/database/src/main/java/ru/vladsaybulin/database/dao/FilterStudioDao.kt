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