package ru.vladsaybulin.core.navigation.args

import ru.vladsaybulin.model.common.Image

data class ImageViewArgs(
    val images: List<Image>,
    val initialIndex: Int,
    val isSingle: Boolean
)