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

package ru.vladsaybulin.network.models.related

import ru.vladsaybulin.model.related.RelationType
import ru.vladsaybulin.network.models.anime.NetworkAnime
import ru.vladsaybulin.network.models.manga.NetworkManga

data class NetworkRelated(
    val relationType: RelationType,
    val anime: NetworkAnime?,
    val manga: NetworkManga?
)