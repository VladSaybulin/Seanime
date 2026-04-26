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

package ru.vladsaybulin.database.models.character

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.vladsaybulin.database.models.common.ImagePOJO
import ru.vladsaybulin.database.models.common.asExternalModel
import ru.vladsaybulin.model.character.Character

@Entity(tableName = "characters")
data class CharacterEntity(

    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("name_ru")
    val nameRu: String?,

    @Embedded("image_")
    val image: ImagePOJO?
)

fun CharacterEntity.asExternalModel() = Character(
    id = id,
    originalName = name,
    russianName = nameRu,
    poster = image?.asExternalModel()
)