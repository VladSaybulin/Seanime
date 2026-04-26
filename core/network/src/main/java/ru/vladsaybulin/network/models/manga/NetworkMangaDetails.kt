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

package ru.vladsaybulin.network.models.manga

import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.manga.MangaKind
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.network.models.common.NetworkGenre
import ru.vladsaybulin.network.models.common.NetworkIncompleteDate
import ru.vladsaybulin.network.models.common.NetworkImage
import ru.vladsaybulin.network.models.common.NetworkStatisticsItem
import ru.vladsaybulin.network.models.related.NetworkRelated

data class NetworkMangaDetails(
    val id: Long,
    val name: String,
    val nameRu: String?,
    val nameEn: String?,
    val nameJp: String?,
    val alternativeName: List<String>,
    val licenseNameRu: String?,
    val poster: NetworkImage?,
    val kind: MangaKind,
    val score: Float?,
    val status: EntryStatus,
    val chapters: Int,
    val volumes: Int,
    val airedOn: NetworkIncompleteDate?,
    val releasedOn: NetworkIncompleteDate?,
    val descriptionHtml: String?,
    val descriptionSource: String?,
    val genres: List<NetworkGenre>?,
    val scoreStats: List<NetworkStatisticsItem<Int>>?,
    val userRateStatusStats: List<NetworkStatisticsItem<UserRateStatus>>?,
    val publishers: List<NetworkPublisher>,
    val related: List<NetworkRelated>?
)