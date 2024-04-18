package ru.vladsaybulin.core.navigation

import ru.vladsaybulin.model.common.Image

data class ImageViewArgs(
    val images: List<Image>,
    val initialIndex: Int,
    val isSingle: Boolean
)