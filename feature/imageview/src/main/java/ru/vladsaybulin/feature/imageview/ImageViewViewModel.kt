package ru.vladsaybulin.feature.imageview

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.vladsaybulin.core.navigation.ImageViewArgs
import ru.vladsaybulin.model.common.Image
import javax.inject.Inject

class ImageViewViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow<ImageViewState>(ImageViewState.NoSet)
    val state = _state.asStateFlow()

    fun setImages(params: ImageViewArgs) {
        _state.value = ImageViewState.Set(
            params.images,
            params.initialIndex,
            params.isSingle
        )
    }
}

sealed class ImageViewState {
    data object NoSet : ImageViewState()

    data class Set(
        val images: List<Image>,
        val initialIndex: Int,
        val isSingle: Boolean
    ) : ImageViewState()
}