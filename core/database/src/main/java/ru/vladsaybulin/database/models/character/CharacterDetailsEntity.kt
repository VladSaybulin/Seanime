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
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import ru.vladsaybulin.database.models.text.SeanimeTextPOJO

@Entity(
    tableName = "character_details",
    foreignKeys = [
        ForeignKey(
            entity = CharacterEntity::class,
            parentColumns = ["id"],
            childColumns = ["id"]
        )
    ]
)
data class CharacterDetailsEntity(

    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("name_jp")
    val nameJp: String?,

    @ColumnInfo("alt_names")
    val altNames: String?,

    @Embedded("description_")
    val description: SeanimeTextPOJO?,

    @ColumnInfo("description_source")
    val descriptionSource: String?,

    @ColumnInfo("topic_id")
    val topicId: Long?,

    @ColumnInfo("updated_at")
    val updatedAt: Instant,



    )