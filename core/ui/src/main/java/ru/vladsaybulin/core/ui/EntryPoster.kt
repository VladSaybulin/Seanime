package ru.vladsaybulin.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import ru.vladsaybulin.model.Poster

@Composable
fun EntryPoster(
    modifier: Modifier = Modifier,
    poster: Poster?
) {
    Box(modifier = modifier.aspectRatio(PosterAspectRatio)) {
        if (poster == null) {
            NoPosterImage(modifier = Modifier.matchParentSize())
        } else {
            PosterImage(
                url = poster.originalUrl,
                modifier = Modifier.matchParentSize()
            )
        }
    }
}

@Composable
private fun NoPosterImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.no_poster),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun PosterImage(url: String, modifier: Modifier = Modifier) {
    AsyncImage(
        modifier = modifier,
        model = url,
        placeholder = if (LocalInspectionMode.current) {
            painterResource(id = R.drawable.preview_poster_1)
        } else null,
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}

private const val PosterAspectRatio = 3 / 4f