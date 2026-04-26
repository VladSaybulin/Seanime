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

package ru.vladsaybulin.network.models.userrate

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.network.util.serializers.UserRateStatusSerializer

@Serializable
class NetworkUserRateWithTitleLink(
    @SerialName("id") val id: Long,
    @SerialName("target_id") val entryId: Long,
    @SerialName("target_type") val entryType: EntryType,
    @SerialName("score") val score: Int,
    @Serializable(UserRateStatusSerializer::class)
    @SerialName("status")
    val status: UserRateStatus,
    @SerialName("episodes") val episodes: Int,
    @SerialName("chapters") val chapters: Int,
    @SerialName("volumes") val volumes: Int,
    @SerialName("rewatches") val rewatches: Int,
    @SerialName("text") val text: String?,
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("updated_at") val updatedAt: Instant,
)