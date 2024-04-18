package ru.vladsaybulin.data.model

import ru.vladsaybulin.model.anime.Video
import ru.vladsaybulin.network.models.anime.NetworkVideo

fun NetworkVideo.asExternalModel() = Video(
    name = name,
    previewImageUrl = previewImageUrl,
    videoUrl = videoUrl,
    playerUrl = playerUrl,
    kind = kind
)