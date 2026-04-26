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

package ru.vladsaybulin.database.models.manga

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.vladsaybulin.database.models.common.ImagePOJO
import ru.vladsaybulin.database.models.common.IncompleteDatePOJO
import ru.vladsaybulin.database.models.common.asExternalModel
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.manga.MangaKind

@Entity(tableName = "mangas")
class MangaEntity(

    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("name")
    val originalName: String,

    @ColumnInfo("russian_name")
    val russianName: String?,

    @Embedded("image")
    val poster: ImagePOJO?,

    @ColumnInfo("kind")
    val kind: MangaKind,

    @ColumnInfo("status")
    val status: EntryStatus,

    @ColumnInfo("score")
    val score: Float,

    @ColumnInfo("chapters")
    val chapters: Int,

    @ColumnInfo("volumes")
    val volumes: Int,

    @Embedded("aired_on_")
    val airedOn: IncompleteDatePOJO?,

    @Embedded("released_on")
    val releasedOn: IncompleteDatePOJO?
)

fun MangaEntity.asExternalModel() = Manga(
    id = id,
    name = originalName,
    russianName = russianName,
    poster = poster?.asExternalModel(),
    kind = kind,
    status = status,
    score = score,
    chapters = chapters,
    volumes = volumes,
    airedOn = airedOn?.asExternalModel(),
    releasedOn = releasedOn?.asExternalModel()
)