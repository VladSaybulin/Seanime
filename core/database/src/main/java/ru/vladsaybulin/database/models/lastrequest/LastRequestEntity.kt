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

package ru.vladsaybulin.database.models.lastrequest

import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.datetime.Instant
import ru.vladsaybulin.model.request.Request

@Entity(
    tableName = "last_requests",
    primaryKeys = ["type", "target_id"]
)
class LastRequestEntity(

    @ColumnInfo("type")
    val request: Request,

    @ColumnInfo("target_id")
    val targetId: Long,

    @ColumnInfo("date")
    val requestDate: Instant
)