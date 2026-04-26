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

import kotlinx.serialization.Serializable
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.network.util.serializers.UserRateStatusSerializer

@Serializable
class UpdateUserRateRequest(
    @Serializable(UserRateStatusSerializer::class)
    val status: UserRateStatus,
    val score: Int?,
    val episodes: Int?,
    val chapters: Int?,
    val volumes: Int?,
    val rewatches: Int?,
    val text: String?
)