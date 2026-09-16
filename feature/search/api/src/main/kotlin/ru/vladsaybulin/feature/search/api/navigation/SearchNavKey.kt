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

package ru.vladsaybulin.feature.search.api.navigation

import kotlinx.serialization.Serializable
import ru.vladsaybulin.core.navigation.SeanimeNavKey
import ru.vladsaybulin.model.search.SearchType

/**
 * Navigation key for the search feature.
 * @param type The type of search to be performed. If is null then default search will be performed by Anime.
 * @param presetSearchFilter The preset search filter to be applied. If is null then no preset filter will be applied.
 * @param ongoing Whether to filter for ongoing anime. If is null then no filter will be applied.
 */
@Serializable
data class SearchNavKey(
    val type: SearchType?,
    val presetSearchFilter: PresetSearchFilter?,
    val ongoing: Boolean?
) : SeanimeNavKey