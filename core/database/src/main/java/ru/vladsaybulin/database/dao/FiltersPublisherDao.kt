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