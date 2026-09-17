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

package ru.vladsaybulin.feature.list.api.navigation

import kotlinx.serialization.Serializable
import ru.vladsaybulin.core.navigation.Navigator
import ru.vladsaybulin.core.navigation.SeanimeNavKey
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateStatus

/**
 * Navigation key for the list feature.
 * @param userId The ID of the user whose list is to be displayed. Can be null if the list is for the current user.
 * @param titleType Preset type of the titles in the list (Anime or Manga). If null, the list will default to Anime.
 * @param status Preset status of the titles in the list (e.g., Watching, Completed etc.). If null, the list will default to Watching.
 */
@Serializable
data class ListNavKey(
    val userId: Long?,
    val titleType: EntryType?,
    val status: UserRateStatus?
) : SeanimeNavKey

/**
 * Extension function to navigate to the list feature.
 * @param userId The ID of the user whose list is to be displayed. Can be null if the list is for the current user.
 * @param titleType Preset type of the titles in the list (Anime or Manga). If null, the list will default to Anime.
 * @param status Preset status of the titles in the list (e.g., Watching, Completed etc.). If null, the list will default to Watching.
 */
fun Navigator.navigateToList(
    userId: Long? = null,
    titleType: EntryType? = null,
    status: UserRateStatus? = null
) {
    navigateTo(ListNavKey(userId, titleType, status))
}