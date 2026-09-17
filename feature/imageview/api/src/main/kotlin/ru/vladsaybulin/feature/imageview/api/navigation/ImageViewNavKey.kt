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
    @Transient val loadedImages: List<String>? = emptyList()
) : SeanimeNavKey {

    /**
     * Default implementation of toString that ignores the [loadedImages] property, because navigation should ignore it.
     */
    override fun toString(): String {
        return "ImageViewNavKey(source=$source, startImageIndex=$startImageIndex)"
    }

    /**
     * Default implementation of equals that ignores the [loadedImages] property, because navigation should ignore it.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImageViewNavKey) return false

        if (source != other.source) return false
        if (startImageIndex != other.startImageIndex) return false

        return true
    }

    /**
     * Default implementation of hashCode that ignores the [loadedImages] property, because navigation should ignore it.
     */
    override fun hashCode(): Int {
        var result = source.hashCode()
        result = 31 * result + startImageIndex
        return result
    }
}

fun Navigator.navigateToImageView(
    source: ImageViewSource,
    startImageIndex: Int,
    loadedImages: List<String>? = null
) {
    navigateTo(ImageViewNavKey(source, startImageIndex, loadedImages))
}