/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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