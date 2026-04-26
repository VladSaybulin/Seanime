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

package ru.vladsaybulin.network.models.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.network.models.common.NetworkIncompleteDate
import ru.vladsaybulin.network.models.userrate.NetworkUserRate
import ru.vladsaybulin.network.models.common.NetworkImage
import ru.vladsaybulin.network.util.serializers.AnimeKindSerializer
import ru.vladsaybulin.network.util.serializers.EntryStatusSerializer
import ru.vladsaybulin.network.util.serializers.LocalDateToIncompleteDateSerializer

@Serializable
data class NetworkAnime(
    @SerialName("id") val id: Long,
    @SerialName("name") val originalName: String,
    @SerialName("russian") val russianName: String?,
    @SerialName("image") val poster: NetworkImage?,
    @SerialName("kind")
    @Serializable(AnimeKindSerializer::class)
    val kind: AnimeKind,
    @SerialName("status")
    @Serializable(EntryStatusSerializer::class)
    val status: EntryStatus,
    @SerialName("score") val score: Float,
    @SerialName("episodes") val episodes: Int,
    @SerialName("episodes_aired") val episodesAired: Int,
    @Serializable(LocalDateToIncompleteDateSerializer::class)
    @SerialName("aired_on")
    val airedOn: NetworkIncompleteDate?,
    @Serializable(LocalDateToIncompleteDateSerializer::class)
    @SerialName("released_on")
    val releasedOn: NetworkIncompleteDate?,

    //This field is not in the Rest API. Used for Graphql response
    @Transient val userRate: NetworkUserRate? = null
)