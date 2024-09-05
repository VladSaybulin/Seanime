package ru.vladsaybulin.feature.title.screenshots.navigation

import ru.vladsaybulin.model.common.Image

class AnimeScreenshotsNavEvents(
    val showFullscreenImage: (List<Image>, initialIndex: Int) -> Unit,
    val navigateUp: () -> Unit
)