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

package ru.vladsaybulin.network.models.character

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.vladsaybulin.network.models.anime.NetworkAnime
import ru.vladsaybulin.network.models.manga.NetworkManga
import ru.vladsaybulin.network.models.common.NetworkImage
import ru.vladsaybulin.network.models.person.NetworkPerson
import ru.vladsaybulin.network.util.serializers.ImageSerializer

@Serializable
data class NetworkCharacterDetails(

    @SerialName("id")
    val id: Long,

    @SerialName("name")
    val name: String,

    @SerialName("russian")
    val nameRu: String?,

    @Serializable(ImageSerializer::class)
    @SerialName("image")
    val image: NetworkImage?,

    @SerialName("altname")
    val alternativeName: String?,

    @SerialName("japanese")
    val nameJp: String?,

    @SerialName("description_html")
    val descriptionHtml: String?,

    @SerialName("description_source")
    val descriptionSource: String?,

    @SerialName("favoured")
    val favoured: Boolean,

    @SerialName("topic_id")
    val topicId: Long?,

    @SerialName("updated_at")
    val updatedAt: Instant,

    @SerialName("seyu")
    val seyu: List<NetworkPerson>,

    @SerialName("animes")
    val animes: List<NetworkAnime>,

    @SerialName("mangas")
    val mangas: List<NetworkManga>
)