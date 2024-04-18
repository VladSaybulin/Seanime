package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.components.ShikimoriModalBottomSheet
import ru.vladsaybulin.core.ui.entry.EntryListItem
import ru.vladsaybulin.model.person.PersonWithRoles

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AuthorsBottomSheet(
    authors: List<PersonWithRoles>,
    onAuthorClick: (Long) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    ShikimoriModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        AuthorsBottomSheetContent(
            authors = authors,
            onAuthorClick = onAuthorClick,

        )
    }
}

@Composable
private fun AuthorsBottomSheetContent(
    authors: List<PersonWithRoles>,
    onAuthorClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
    ) {
        items(
            items = authors,
            key = { it.person.id }
        ) { (person, _, russianRoles) ->
            EntryListItem(
                name = person.russianName ?: person.originalName,
                poster = person.poster,
                onClick = { onAuthorClick(person.id) },
                detailsContent = {
                    Text(text = russianRoles.joinToString(separator = ", "))
                }
            )
        }
    }
}