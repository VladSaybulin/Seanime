package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.EntryPoster
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.core.ui.drawForegroundGradientScrim
import ru.vladsaybulin.model.common.Image

internal fun LazyListScope.poster(
    poster: Image?,
    topSpace: Dp,
    onPosterClick: () -> Unit
) {
    item(key = "poster") {
        EntryDetailsPoster(
            poster = poster,
            topSpaceDp = topSpace,
            onPosterClick = onPosterClick
        )
    }
}

@Composable
private fun EntryDetailsPoster(
    poster: Image?,
    topSpaceDp: Dp,
    onPosterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        if (poster != null) {
            PosterBackground(
                poster = poster,
                modifier = Modifier.matchParentSize()
            )
        }

        EntryPoster(
            poster = poster,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .align(Alignment.TopCenter)
                .padding(top = topSpaceDp)
                .shadow(4.dp, shape = ShikimoriTheme.shapes.large, clip = true)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = poster != null,
                    role = Role.Image,
                    onClick = onPosterClick

                )
        )
    }
}

@Composable
private fun PosterBackground(
    poster: Image,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = poster.originalUrl,
        contentDescription = null,
        modifier = modifier
            .blur(16.dp)
            .drawForegroundGradientScrim(
                startColor = ShikimoriTheme.colorScheme.surface.copy(alpha = 0.5f),
                stopColor = ShikimoriTheme.colorScheme.surface
            ),
        placeholder = if (LocalInspectionMode.current) {
            painterResource(id = R.drawable.preview_poster_1)
        } else null,
        contentScale = ContentScale.Crop
    )
}

@Composable
@Preview
fun PosterPreview() {
    ShikimoriTheme {
        EntryDetailsPoster(
            poster = Image("", ""),
            topSpaceDp = 48.dp,
            onPosterClick = { }
        )
    }
}