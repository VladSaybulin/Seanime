package ru.vladsaybulin.core.ui.entry.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.previewAnimes

class AnimePreviewProvider : PreviewParameterProvider<Anime> {
    override val values: Sequence<Anime> = previewAnimes.asSequence()
}