package ru.vladsaybulin.feature.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.model.Person
import ru.vladsaybulin.model.PersonWithRoles
import ru.vladsaybulin.model.Poster
import ru.vladsaybulin.core.ui.R as uiR

@Composable
fun AuthorCard(personWithRoles: PersonWithRoles) {
    Surface(
        shape = ShikimoriTheme.shapes.large,
        shadowElevation = 2.dp,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .padding(end = 16.dp)
                .height(IntrinsicSize.Max)
        ) {
            AuthorPoster(poster = personWithRoles.person.poster)
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(min = 100.dp)
                    .width(IntrinsicSize.Min)
            ) {
                Text(
                    text = personWithRoles.person.run { russianName ?: originalName },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = ShikimoriTheme.typography.labelLarge,
                    modifier = Modifier.width(IntrinsicSize.Max)
                )
                Text(
                    text = personWithRoles.russianRoles.joinToString(separator = ", "),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = ShikimoriTheme.typography.labelSmall,
                    modifier = Modifier.fillMaxWidth().alpha(0.6f)
                )
            }
        }
    }
}

@Composable
private fun AuthorPoster(poster: Poster?) {
    Box(
        modifier = Modifier
            .size(PosterSize)
            .clip(CircleShape)
            .run {
                if (poster == null) {
                    this then Modifier.background(
                        color = ShikimoriTheme.colorScheme.surfaceColorAtElevation(8.dp)
                    )
                } else this
            }
    ) {
        if (poster == null) {
            Icon(
                imageVector = ShikimoriIcons.Person,
                contentDescription = null,
                tint = ShikimoriTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            AsyncImage(
                model = poster.originalUrl,
                contentDescription = null,
                placeholder = if (LocalInspectionMode.current) {
                    painterResource(id = uiR.drawable.preview_poster_1)
                } else null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        }
    }
}

@Preview
@Composable
fun AuthorCardPreview() {
    ShikimoriTheme {
        AuthorCard(
            PersonWithRoles(
                person = Person(
                    id = 0,
                    originalName = "Author name",
                    russianName = "Author russian name",
                    poster = Poster("")
                ),
                englishRoles = listOf("Director"),
                russianRoles = listOf("Режиссёр")
            )
        )
    }
}

@Preview
@Composable
fun AuthorCardWithoutPosterPreview() {
    ShikimoriTheme {
        AuthorCard(
            PersonWithRoles(
                person = Person(
                    id = 0,
                    originalName = "Author name",
                    russianName = "Author russian name",
                    poster = null
                ),
                englishRoles = listOf("Director"),
                russianRoles = listOf("Режиссёр")
            )
        )
    }
}

@Preview
@Composable
fun AuthorCardManyRolesPreview() {
    ShikimoriTheme {
        AuthorCard(
            PersonWithRoles(
                person = Person(
                    id = 0,
                    originalName = "Author name",
                    russianName = "Author russian name",
                    poster = Poster("")
                ),
                englishRoles = listOf("Director"),
                russianRoles = listOf("Режиссёр", "Раскадровка", "Рисовка")
            )
        )
    }
}

@Preview
@Composable
fun AuthorCardShortNamePreview() {
    ShikimoriTheme {
        AuthorCard(
            PersonWithRoles(
                person = Person(
                    id = 0,
                    originalName = "Author",
                    russianName = "Author",
                    poster = Poster("")
                ),
                englishRoles = listOf("Director"),
                russianRoles = listOf("Режиссёр", "Раскадровка", "Рисовка")
            )
        )
    }
}

private val PosterSize = 48.dp