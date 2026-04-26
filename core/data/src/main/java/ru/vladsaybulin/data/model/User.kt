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

package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.user.UserEntity
import ru.vladsaybulin.database.models.user.UserImagePOJO
import ru.vladsaybulin.network.models.user.NetworkBriefUser
import ru.vladsaybulin.network.models.user.UserImageDto

fun NetworkBriefUser.asExternalModel() = UserEntity(
    id = id,
    nickname = nickname,
    avatarUrl = avatarUrl,
    image = image.asPOJO(),
    lastOnlineAt = lastOnlineAt,
    url = url
)

private fun UserImageDto.asPOJO() = UserImagePOJO(
    x160Url = x160Url,
    x148Url = x148Url,
    x80Url = x80Url,
    x64Url = x64Url,
    x48Url = x48Url,
    x32Url = x32Url,
    x16Url = x16Url
)