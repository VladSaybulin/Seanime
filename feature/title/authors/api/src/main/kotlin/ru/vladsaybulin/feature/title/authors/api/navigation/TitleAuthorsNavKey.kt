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

package ru.vladsaybulin.feature.title.authors.api.navigation

import kotlinx.serialization.Serializable
import ru.vladsaybulin.core.navigation.SeanimeNavKey
import ru.vladsaybulin.model.common.EntryType

/**
 * Navigation key for the title authors feature.
 * @param titleType The type of the title (Anime or Manga).
 * @param titleId The ID of the title.
 */
@Serializable
data class TitleAuthorsNavKey(val titleType: EntryType, val titleId: Long) : SeanimeNavKey