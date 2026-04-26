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

package ru.vladsaybulin.database.models.userrate

import androidx.room.Embedded
import androidx.room.Relation
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.anime.asExternalModel
import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.database.models.manga.asExternalModel
import ru.vladsaybulin.model.userrate.UserRateWithEntry

data class PopulatedUserRate(

    @Embedded
    val userRateEntity: UserRateEntity,

    @Relation(
        entity = AnimeEntity::class,
        parentColumn = "anime_id",
        entityColumn = "id"
    )
    val animeDbo: AnimeEntity?,

    @Relation(
        entity = MangaEntity::class,
        parentColumn = "manga_id",
        entityColumn = "id"
    )
    val mangaDbo: MangaEntity?,
)

fun PopulatedUserRate.asExternalModel() = UserRateWithEntry(
    anime = animeDbo?.asExternalModel(),
    manga = mangaDbo?.asExternalModel(),
    userRate = userRateEntity.asExternalModel()
)