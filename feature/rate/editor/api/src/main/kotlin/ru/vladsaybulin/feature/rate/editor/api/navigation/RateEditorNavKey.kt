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

package ru.vladsaybulin.feature.rate.editor.api.navigation

import kotlinx.serialization.Serializable
import ru.vladsaybulin.core.navigation.Navigator
import ru.vladsaybulin.core.navigation.SeanimeNavKey

/**
 * Navigation key for the rate editor feature.
 * @param rateId The ID of the rate to be edited.
 */
@Serializable
data class RateEditorNavKey(val rateId: Long) : SeanimeNavKey

/**
 * Extension function to navigate to the rate editor feature.
 * @param rateId The ID of the rate to be edited.
 */
fun Navigator.navigateToRateEditor(rateId: Long) {
    navigateTo(RateEditorNavKey(rateId))
}