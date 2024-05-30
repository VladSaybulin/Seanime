package ru.vladsaybulin.feature.authors

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.entry.EntryListItem
import ru.vladsaybulin.core.ui.strings.personRoleString
import ru.vladsaybulin.model.person.Person
import ru.vladsaybulin.model.person.PersonWithRoles

@Composable
fun AuthorItem(
    author: PersonWithRoles,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    EntryListItem(
        name = author.person.run { russianName ?: originalName },
        imageUrl = author.person.poster?.originalUrl,
        onClick = onClick,
        imageWidth = 72.dp,
        modifier = modifier
    ) {
        Text(
            text = author.roles
                // Can't move map in joinToString because no Composable context
                .map { personRoleString(personRole = it) }
                .joinToString(separator = ", "),
            modifier = Modifier.alpha(0.5f)
        )
    }
}

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