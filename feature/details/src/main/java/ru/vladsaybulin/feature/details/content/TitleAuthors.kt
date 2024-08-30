package ru.vladsaybulin.feature.details.content

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
import ru.vladsaybulin.core.designsystem.components.ShikimoriCarousel
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.core.ui.strings.personRoleString
import ru.vladsaybulin.model.common.DataSlice
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.person.Person
import ru.vladsaybulin.model.person.PersonWithRoles

@Composable
fun TitleAuthors(
    authors: List<PersonWithRoles>,
    onAuthorClick: (Person) -> Unit
) {
    ShikimoriCarousel(items = authors) {
        AuthorCard(
            personWithRoles = it,
            onClick = { onAuthorClick(it.person) }
        )
    }
}

@Composable
private fun AuthorCard(
    personWithRoles: PersonWithRoles,
    onClick: () -> Unit
) {
    Surface(
        shape = SeanimeTheme.shapes.large,
        shadowElevation = 2.dp,
        tonalElevation = 1.dp,
        onClick = onClick
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
                    style = SeanimeTheme.typography.labelLarge,
                    modifier = Modifier.width(IntrinsicSize.Max)
                )
                Text(
                    text = personWithRoles.roles
                        // Can't move map in joinToString because no Composable context
                        .map { personRoleString(personRole = it) }
                        .joinToString(separator = ", "),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = SeanimeTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.6f)
                )
            }
        }
    }
}

@Composable
private fun AuthorPoster(poster: Image?) {
    Box(
        modifier = Modifier
            .size(PosterSize)
            .clip(CircleShape)
            .run {
                if (poster == null) {
                    this then Modifier.background(
                        color = SeanimeTheme.colorScheme.surfaceColorAtElevation(8.dp)
                    )
                } else this
            }
    ) {
        if (poster == null) {
            Icon(
                imageVector = SeanimeIcons.Person,
                contentDescription = null,
                tint = SeanimeTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            AsyncImage(
                model = poster.originalUrl,
                contentDescription = null,
                placeholder = if (LocalInspectionMode.current) {
                    painterResource(id = R.drawable.preview_poster_1)
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
    SeanimeTheme {
        AuthorCard(
            PersonWithRoles(
                person = Person(
                    id = 0,
                    originalName = "Author name",
                    russianName = "Author russian name",
                    poster = Image("", ""),
                ),
                roles = listOf("Director"),
                isMain = true
            ),
            onClick = { }
        )
    }
}

@Preview
@Composable
fun AuthorCardWithoutPosterPreview() {
    SeanimeTheme {
        AuthorCard(
            PersonWithRoles(
                person = Person(
                    id = 0,
                    originalName = "Author name",
                    russianName = "Author russian name",
                    poster = null
                ),
                roles = listOf("Director"),
                isMain = true
            ),
            onClick = { }
        )
    }
}

@Preview
@Composable
fun AuthorCardManyRolesPreview() {
    SeanimeTheme {
        AuthorCard(
            PersonWithRoles(
                person = Person(
                    id = 0,
                    originalName = "Author name",
                    russianName = "Author russian name",
                    poster = Image("", ""),
                ),
                roles = listOf("Director"),
                isMain = true
            ),
            onClick = { }
        )
    }
}

@Preview
@Composable
fun AuthorCardShortNamePreview() {
    SeanimeTheme {
        AuthorCard(
            PersonWithRoles(
                person = Person(
                    id = 0,
                    originalName = "Author",
                    russianName = "Author",
                    poster = Image("", ""),
                ),
                roles = listOf("Director", "Original Creator"),
                isMain = true
            ),
            onClick = { }
        )
    }
}

private val PosterSize = 48.dp