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

package ru.vladsaybulin.feature.imageview.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.Lazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.vladsaybulin.core.domain.repository.AnimeRepository
import ru.vladsaybulin.core.domain.repository.MangaRepository
import ru.vladsaybulin.feature.imageview.api.navigation.ImageViewNavKey
import ru.vladsaybulin.feature.imageview.api.navigation.ImageViewSource
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.Image

/**
 * ViewModel for the ImageView feature. It handles loading images based on the provided [ImageViewNavKey].
 * If the images are already provided in the key, it uses them directly; otherwise, it fetches them from the appropriate source.
 */
@HiltViewModel(assistedFactory = ImageViewViewModel.Factory::class)
class ImageViewViewModel @AssistedInject constructor(
    private val animeRepository: Lazy<AnimeRepository>,
    private val mangaRepository: Lazy<MangaRepository>,
    @Assisted private val key: ImageViewNavKey
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(key: ImageViewNavKey): ImageViewViewModel
    }

    val initialIndex: Int = key.startImageIndex

    private val loadedImageSetOrNull = key.loadedImages
    val loadState: StateFlow<ImageViewLoadState> = if (loadedImageSetOrNull.isNullOrEmpty()) {
        imagesFromSourceStream(key.source)
            .map {
                ImageViewLoadState.Success(it)
            }
            .stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5000),
                initialValue = ImageViewLoadState.Loading
            )
    } else {
        MutableStateFlow(ImageViewLoadState.Success(loadedImageSetOrNull))
    }

    private fun imagesFromSourceStream(source: ImageViewSource): Flow<List<String>> {
        return when (source) {
            is ImageViewSource.AnimeScreenshots ->
                animeRepository.get()
                    .getAnimeScreenshots(source.animeId)
                    .map { images -> images.map(Image::originalUrl) }

            is ImageViewSource.TitlePoster ->
                titlePosterStream(source.titleType, source.titleId)
        }
    }

    private fun titlePosterStream(titleType: EntryType, titleId: Long): Flow<List<String>> {
        return when (titleType) {
            EntryType.Anime -> animeRepository.get()
                .getAnimeDetailsStream(titleId) //TODO create poster only fetch method
                .map { anime -> listOfNotNull(anime.poster?.originalUrl) }

            EntryType.Manga -> mangaRepository.get()
                .getMangaDetailsStream(titleId) //TODO create poster only fetch method
                .map { manga -> listOfNotNull(manga.poster?.originalUrl) }
        }
    }
}

sealed class ImageViewLoadState {
    data object Loading : ImageViewLoadState()
    data class Success(val images: List<String>) : ImageViewLoadState()
}