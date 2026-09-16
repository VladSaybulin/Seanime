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

package ru.vladsaybulin.feature.profile.api.navigation

import kotlinx.serialization.Serializable
import ru.vladsaybulin.core.navigation.SeanimeNavKey

/**
 * Navigation key for the profile feature.
 * @param userId The ID of the user whose profile is to be displayed. Can be null if the profile is for the current user.
 */
@Serializable
data class ProfileNavKey(val userId: Long?) : SeanimeNavKey