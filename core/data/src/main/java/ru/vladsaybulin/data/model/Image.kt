package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.common.ImagePOJO
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.network.models.common.NetworkImage

fun Image.asPOJO() = ImagePOJO(originalUrl, previewUrl)

fun NetworkImage.asPOJO() = ImagePOJO(originalUrl, previewUrl)

fun NetworkImage.asExternalModel() = Image(originalUrl, previewUrl)