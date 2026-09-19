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
import ru.vladsaybulin.model.common.EntryType

/**
 * Represents the pointer to source of images to be displayed in the image view feature.
 * This can be extended to include different types of image sources in the future.
 */
@Serializable
sealed class ImageViewSource {

    /** Represents a source of images that are screenshots of a specific anime. */
    @Serializable
    data class AnimeScreenshots(val animeId: Long) : ImageViewSource()

    /** Represents a source of anime/manga poster */
    @Serializable
    data class TitlePoster(val titleType: EntryType, val titleId: Long) : ImageViewSource()
}