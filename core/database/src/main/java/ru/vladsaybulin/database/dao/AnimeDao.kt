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
import androidx.room.Upsert
import ru.vladsaybulin.database.models.userrate.AnimeRateContextDb
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.common.ImagePOJO

@Dao
interface AnimeDao {

    @Query("SELECT * FROM animes WHERE id = :animeId")
    suspend fun getAnimeById(animeId: Long): AnimeEntity

    @Query("SELECT status, max(episodes, episodes_aired) AS episodes FROM animes WHERE id = :animeId")
    suspend fun getAnimeRateContext(animeId: Long): AnimeRateContextDb

    @Query("SELECT imageoriginal AS original, imagepreview AS preview FROM animes WHERE id = :animeId")
    fun getPosterStream(animeId: Long): Flow<ImagePOJO?>

    @Upsert
    suspend fun upsertAnime(anime: AnimeEntity)

    @Upsert
    suspend fun upsertAnimes(animes: List<AnimeEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreAnimes(animes: List<AnimeEntity>)
}