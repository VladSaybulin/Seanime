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

package ru.vladsaybulin.model.title

import ru.vladsaybulin.model.Entry
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.common.IncompleteDate

data class Title(
    override val id: Long,
    val type: TitleType,
    override val name: String,
    override val nameRu: String?,
    override val poster: Image?,
    val kind: TitleKind,
    val status: TitleStatus,
    val score: Float,
    val episodes: Int,
    val episodesAired: Int,
    val chapters: Int,
    val volumes: Int,
    val airedOn: IncompleteDate?,
    val releasedOn: IncompleteDate?
) : Entry {
    override val entryType: Entry.Type = Entry.Type.Title
}