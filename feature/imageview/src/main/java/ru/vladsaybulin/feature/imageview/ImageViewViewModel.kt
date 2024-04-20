package ru.vladsaybulin.feature.imageview

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.vladsaybulin.core.navigation.args.ImageViewArgs
import ru.vladsaybulin.model.common.Image
import javax.inject.Inject

@HiltViewModel
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