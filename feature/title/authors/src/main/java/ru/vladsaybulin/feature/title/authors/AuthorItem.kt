package ru.vladsaybulin.feature.title.authors

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.strings.personRoleString
import ru.vladsaybulin.model.person.Person
import ru.vladsaybulin.model.person.PersonWithRoles
import ru.vladsaybulin.core.ui2.entry.EntryItemDefaults
import ru.vladsaybulin.core.ui2.entry.EntryListItem

@Composable
fun AuthorItem(
    author: PersonWithRoles,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    EntryListItem(
        name = author.person.originalName,
        russianName = author.person.russianName,
        poster = author.person.poster,
        onClick = onClick,
        posterWidth = PersonPosterWidth,
        modifier = modifier,
        colors = EntryItemDefaults.SurfaceColors
    ) {
        Text(
            text = localizedRoles(author.roles),
            modifier = Modifier.alpha(0.5f)
        )
    }
}

@Composable
private fun localizedRoles(rolesEn: List<String>): String = rolesEn
        // Can't move map in joinToString because no Composable context
        .map { personRoleString(personRole = it) }
        .joinToString(separator = ", ")

@Preview
@Composable
fun AuthorItemPreview() {
    SeanimeTheme {
        AuthorItem(
            author = PersonWithRoles(
                person = Person(
                    id = 0,
                    originalName = "Takaomi Kanasaki",
                    russianName = "Такаоми Канасаки",
                    poster = null
                ),
                roles = listOf(
                    "Chief Animation Director",
                    "Animation Director"
                ),
                isMain = false
            ),
            onClick = { }
        )
    }
}

private val PersonPosterWidth = 72.dp