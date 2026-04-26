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
import ru.vladsaybulin.database.models.common.SeasonPOJO
import ru.vladsaybulin.database.models.stats.StatsProto
import ru.vladsaybulin.database.models.text.SeanimeTextPOJO
import ru.vladsaybulin.model.common.StatisticsItem
import ru.vladsaybulin.model.userrate.UserRateStatus

@Entity(tableName = "manga_details")
data class MangaDetailsEntity(

    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("english")
    val nameEn: String?,

    @ColumnInfo("japanese")
    val nameJp: String?,

    @ColumnInfo("synonyms")
    val altNames: List<String>,

    @ColumnInfo("license_name")
    val licenseNameRu: String?,

    @Embedded("description_")
    val description: SeanimeTextPOJO?,

    @ColumnInfo("description_source")
    val descriptionSource: String?,

    @ColumnInfo("score_stats")
    val scoreStats: StatsProto<Int>,

    @ColumnInfo("status_stats")
    val statusStats: StatsProto<UserRateStatus>
)