package ru.vladsaybulin.feature.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.model.PersonWithRoles

@Composable
fun AuthorsCarousel(
    authors: List<PersonWithRoles>,
    modifier: Modifier = Modifier,
    horizontalContentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.authors),
            style = ShikimoriTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontalContentPadding)
        )
        LazyRow(contentPadding = horizontalContentPadding, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(
                items = authors,
                key = { it.person.id }
            ) {
                AuthorCard(personWithRoles = it)
            }
        }
    }
}