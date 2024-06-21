package ru.vladsaybulin.feature.imageview

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.mutableStateOf
import ru.vladsaybulin.model.common.Image

class FullScreenImageState {

    private val _images = mutableStateOf(emptyList<Image>())
    val images: List<Image>
        get() = _images.value

    internal val pagerState = PagerState { _images.value.size }

    private val animatableOpacity = Animatable(initialValue = 0f)
    val opacity
        get() = animatableOpacity.value

    val isVisible: Boolean
        get() = animatableOpacity.value != 0f

    suspend fun show(newImages: List<Image>, startIndex: Int) {
        _images.value = newImages
        pagerState.requestScrollToPage(startIndex)
        animatableOpacity.animateTo(1f)
    }

    suspend fun hide() {
        animatableOpacity.animateTo(0f)
    }
}