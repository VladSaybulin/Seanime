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

package ru.vladsaybulin.model.manga

import ru.vladsaybulin.model.annotatedtext.SeanimeText
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.common.IncompleteDate
import ru.vladsaybulin.model.common.StatisticsItem
import ru.vladsaybulin.model.genre.Genre
import ru.vladsaybulin.model.userrate.UserRateStatus

data class MangaDetails(
    val id: Long,
    val originalName: String,
    val russianName: String?,
    val englishName: String?,
    val japaneseName: String?,
    val alternativeName: List<String>,
    val licenseNameRu: String?,
    val poster: Image?,
    val kind: MangaKind,
    val score: Float?,
    val status: EntryStatus,
    val chapters: Int,
    val volumes: Int,
    val airedOn: IncompleteDate?,
    val releasedOn: IncompleteDate?,
    val description: SeanimeText?,
    val descriptionSource: String?,
    val genres: List<Genre>,
    val scoreStats: List<StatisticsItem<Int>>?,
    val userRateStatusStats: List<StatisticsItem<UserRateStatus>>?,
    val publishers: List<Publisher>
)