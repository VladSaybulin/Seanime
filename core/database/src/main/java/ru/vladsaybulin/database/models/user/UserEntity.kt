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

package ru.vladsaybulin.database.models.user

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import ru.vladsaybulin.model.user.BriefUser

@Entity(tableName = "users")
data class UserEntity(

    @ColumnInfo("id")
    @PrimaryKey
    val id: Long,

    @ColumnInfo("nickname")
    val nickname: String,

    @ColumnInfo("avatar")
    val avatarUrl: String,

    @Embedded("image_")
    val image: UserImagePOJO,

    @ColumnInfo("last_online_at")
    val lastOnlineAt: Instant,

    @ColumnInfo("url")
    val url: String
)

fun UserEntity.asExternalModel() = BriefUser(
    id = id,
    nickname = nickname,
    avatarUrl = avatarUrl,
    image = image.asExternalModel(),
    lastOnlineAt = lastOnlineAt,
    url = url
)