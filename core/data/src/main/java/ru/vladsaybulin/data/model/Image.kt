package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.common.ImageEntity
import ru.vladsaybulin.model.common.Image

fun Image.asEntity() = ImageEntity(originalUrl, previewUrl)