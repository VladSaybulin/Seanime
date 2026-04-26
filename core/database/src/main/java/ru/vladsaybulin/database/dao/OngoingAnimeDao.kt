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
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.anime.OngoingAnimeEntity

@Dao
interface OngoingAnimeDao {

    @Query(
        value = """
            SELECT animes.* 
            FROM ongoing_animes
            INNER JOIN animes ON anime_id = animes.id
            LIMIT :limit
        """
    )
    fun getOngoingAnime(limit: Int): Flow<List<AnimeEntity>>

    @Insert
    suspend fun insertAll(ongoingAnime: List<OngoingAnimeEntity>)

    @Query("DELETE FROM ongoing_animes")
    suspend fun deleteAll()


}