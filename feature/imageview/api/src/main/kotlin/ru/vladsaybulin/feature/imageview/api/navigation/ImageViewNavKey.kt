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
import kotlinx.serialization.Transient
import ru.vladsaybulin.core.navigation.LoadedData
import ru.vladsaybulin.core.navigation.Navigator
import ru.vladsaybulin.core.navigation.SeanimeNavKey

/**
 * Navigation key for the image view feature.
 * [loadedImages] is not serialized into a key. To restore it, [source] is used, which specifies how to load images from repositories.
 * @param source The source of the images to be displayed.
 * @param loadedImages The loaded set of images to be displayed. May be null if the images have not been loaded yet.
 */
@Serializable
data class ImageViewNavKey(
    val source: ImageViewSource,
    val startImageIndex: Int,
    @Transient val loadedImages: LoadedData<List<String>> = LoadedData.ofNull()
) : SeanimeNavKey

fun Navigator.navigateToImageView(
    source: ImageViewSource,
    startImageIndex: Int,
    loadedImages: List<String>? = null
) {
    navigateTo(ImageViewNavKey(source, startImageIndex, LoadedData(loadedImages)))
}