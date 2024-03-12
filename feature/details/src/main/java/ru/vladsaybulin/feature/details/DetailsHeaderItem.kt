package ru.vladsaybulin.feature.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.EntryPoster
import ru.vladsaybulin.core.ui.drawForegroundGradientScrim
import ru.vladsaybulin.feature.details.model.DetailsHeader
import ru.vladsaybulin.model.Poster

fun LazyListScope.detailsHeaderItem(
    header: DetailsHeader,
    modifier: Modifier = Modifier,
    itemKey: String = DefaultItemKey,
    topSpacing: Dp = DefaultTopSpacing,
    horizontalPadding: PaddingValues = DefaultHorizontalPadding
) {
    item(key = itemKey) {
        DetailsHeaderContent(
            header = header,
            modifier = modifier,
            topSpacing = topSpacing,
            horizontalPadding = horizontalPadding
        )
    }
}

@Composable
fun DetailsHeaderContent(
    header: DetailsHeader,
    modifier: Modifier = Modifier,
    topSpacing: Dp = DefaultTopSpacing,
    horizontalPadding: PaddingValues = DefaultHorizontalPadding
) {
    Box(modifier = modifier.fillMaxWidth()) {
        if (header.poster != null) {
            DetailsHeaderBackground(
                poster = header.poster,
                modifier = Modifier.matchParentSize()
            )
        }
        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(topSpacing))
            EntryPoster(
                poster = header.poster,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(horizontalPadding)
                    .shadow(4.dp, shape = ShikimoriTheme.shapes.large, clip = true)
            )
            Spacer(modifier = Modifier.height(PosterSpace))
            DetailsHeaderName(
                russianName = header.russianName,
                originalName = header.name,
                modifier = Modifier.padding(horizontalPadding)
            )
            Spacer(modifier = Modifier.height(PosterSpace))
        }
    }
}

@Composable
private fun DetailsHeaderName(
    russianName: String?,
    originalName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = russianName ?: originalName,
            style = ShikimoriTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        if (russianName != null) {
            Text(
                text = originalName,
                style = ShikimoriTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = LocalContentColor.current.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun DetailsHeaderBackground(
    poster: Poster,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = poster.originalUrl,
        contentDescription = null,
        modifier = modifier
            .blur(16.dp)
            .drawForegroundGradientScrim(
                startColor = ShikimoriTheme.colorScheme.surface.copy(alpha = 0.5f),
                targetColor = ShikimoriTheme.colorScheme.surface
            ),
        placeholder = if (LocalInspectionMode.current) {
            painterResource(id = ru.vladsaybulin.core.ui.R.drawable.preview_poster_1)
        } else null,
        contentScale = ContentScale.Crop
    )
}

@Preview
@Composable
fun DetailsHeaderContentPreview() {
    ShikimoriTheme {
        DetailsHeaderContent(
            header = DetailsHeader(
                poster = Poster(
                    originalUrl = "",
                    previewUrl = ""
                ),
                name = "Original name",
                russianName = "Русское название",
                animeRating = null
            )
        )
    }
}

private const val DefaultItemKey = "header"

private val PosterSpace = 16.dp

private val DefaultTopSpacing = 64.dp
private val DefaultHorizontalPadding = PaddingValues(horizontal = 16.dp)