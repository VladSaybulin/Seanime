package ru.vladsaybulin.core.ui2.entry

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import ru.vladsaybulin.core.ui2.entry.R

@Composable
fun EntryPoster(
    posterUrl: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Crop,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) {
    val painter = if (posterUrl != null) {
        rememberAsyncImagePainter(
            model = posterUrl,
            contentScale = contentScale
        )
    } else {
        painterResource(R.drawable.entry_no_poster)
    }

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier.aspectRatio(EntryPosterAspectRatio),
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter
    )
}

private const val EntryPosterAspectRatio = 3 / 4f