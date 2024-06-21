package ru.vladsaybulin.feature.authors

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.feature.authors.navigation.TitleAuthorsNavEvents
import ru.vladsaybulin.model.person.PersonWithRoles

@Composable
fun AuthorsScreen(
    navEvents: TitleAuthorsNavEvents,
    viewModel: AuthorsViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AuthorsScreen(
        uiState = uiState,
        onAuthorClick = { navEvents.navigateToPerson(it.person.id) },
        onBack = navEvents.navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthorsScreen(
    uiState: AuthorsUiState,
    onAuthorClick: (PersonWithRoles) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AuthorsTopBar(
                onBack = onBack,
                scrollBehavior = scrollBehavior
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Box(
            modifier = Modifier.padding(padding)
        ) {
            when (uiState) {
                AuthorsUiState.Loading -> Unit

                is AuthorsUiState.Success -> AuthorsContent(
                    uiState = uiState,
                    onAuthorClick = onAuthorClick
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthorsTopBar(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.feature_authors_title))
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = SeanimeIcons.ArrowBack,
                    contentDescription = stringResource(id = R.string.feature_authors_back_icon)
                )
            }
        },
        modifier = modifier,
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun AuthorsContent(
    uiState: AuthorsUiState.Success,
    onAuthorClick: (PersonWithRoles) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp)
    ) {
        authors(
            authors = uiState.authors,
            onAuthorClick = onAuthorClick
        )
    }
}

private fun LazyListScope.authors(
    authors: List<PersonWithRoles>,
    onAuthorClick: (PersonWithRoles) -> Unit
) {
    items(authors) {
        AuthorItem(
            author = it,
            onClick = { onAuthorClick(it) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}