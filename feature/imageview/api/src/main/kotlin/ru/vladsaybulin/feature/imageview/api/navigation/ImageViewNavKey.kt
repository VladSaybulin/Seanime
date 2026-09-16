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

package ru.vladsaybulin.feature.imageview.api.navigation

import kotlinx.serialization.Serializable
import ru.vladsaybulin.core.navigation.SeanimeNavKey

/**
 * Navigation key for the image view feature.
 * If provided [startImageUrl], but [source] is null, the image view will display only that image.
 * @param source The source of the images to be displayed. Can be null if [startImageUrl] is provided.
 * @param imageSetSize The size of the image set which source provides.
 *  If size unknown then 0. If [source] is null, this value is ignored.
 * @param startImageIndex The index of the image to start displaying. If [imageSetSize] is 0, this value is ignored.
 * @param startImageUrl The URL of the image to start displaying. Can be null if [source] is provided.
 */
@Serializable
data class ImageViewNavKey(
    val source: ImageViewSource?,
    val imageSetSize: Int,
    val startImageIndex: Int,
    val startImageUrl: String?,
) : SeanimeNavKey {
    init {
        require(source != null || startImageUrl != null) {
            "Either source or startImageUrl must be provided"
        }

        require(imageSetSize >= 0) { "imageSetSize must be non-negative" }
        require(startImageIndex >= 0) { "startImageIndex must be non-negative" }
    }
}