package ru.vladsaybulin.feature.imageview

import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

class ImageViewViewModel @Inject constructor() : ViewModel() {

    private var images: ImmutableList<Image>? = null
    val requireImages: ImmutableList<Image>
        get() = checkNotNull(images)

    var initialImage = 0
        private set

    fun setup(
        images: List<Image>,
        initialImage: Int
    ) {
        this.images = images.toImmutableList()
        this.initialImage = initialImage
    }
}