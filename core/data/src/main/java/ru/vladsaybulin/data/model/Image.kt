package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.common.ImagePOJO
import ru.vladsaybulin.model.common.Image

fun Image.asEntity() = ImagePOJO(originalUrl, previewUrl)