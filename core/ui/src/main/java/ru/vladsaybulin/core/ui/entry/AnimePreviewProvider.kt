package ru.vladsaybulin.core.ui.entry

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.vladsaybulin.model.Anime
import ru.vladsaybulin.model.previewAnimes

class AnimePreviewProvider : PreviewParameterProvider<Anime> {
    override val values: Sequence<Anime> = previewAnimes.asSequence()
}