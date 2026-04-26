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

package ru.vladsaybulin.network.models.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserImageDto(
    @SerialName("x160") val x160Url: String,
    @SerialName("x148") val x148Url: String,
    @SerialName("x80") val x80Url: String,
    @SerialName("x64") val x64Url: String,
    @SerialName("x48") val x48Url: String,
    @SerialName("x32") val x32Url: String,
    @SerialName("x16") val x16Url: String
)
