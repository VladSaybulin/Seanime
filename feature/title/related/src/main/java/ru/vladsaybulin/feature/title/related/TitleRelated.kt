package ru.vladsaybulin.feature.title.related

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.ui.LocalScreenContentPadding
import ru.vladsaybulin.core.ui.RelatedTitleItem
import ru.vladsaybulin.feature.titlerelated.R
import ru.vladsaybulin.feature.title.related.navigation.TitleRelatedNavEvents
import ru.vladsaybulin.model.common.EntryType

@Composable
fun TitleRelatedRoute(
    navEvents: TitleRelatedNavEvents,
    viewModel: TitleRelatedViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    TitleRelatedScreen(
        state = state,
        navEvents = navEvents
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TitleRelatedScreen(
    state: TitleRelatedUiState,
    navEvents: TitleRelatedNavEvents
) {
    val topBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.feature_titlerelated_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = navEvents.navigateUp) {
                        Icon(
                            imageVector = SeanimeIcons.ArrowBack,
                            contentDescription = stringResource(id = R.string.feature_titlerelated_back)
                        )
                    }
                },
                scrollBehavior = topBarScrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(topBarScrollBehavior.nestedScrollConnection)
    ) { scaffoldPadding ->
        Box(
            modifier = Modifier
                .padding(scaffoldPadding)
                .padding(LocalScreenContentPadding.current)
        ) {
            if (state is TitleRelatedUiState.Success) {
                TitleRelatedContent(state = state, onTitleClick = navEvents.navigateToTitleDetails)
            }
        }
    }
}

@Composable
private fun TitleRelatedContent(
    state: TitleRelatedUiState.Success,
    onTitleClick: (EntryType, Long) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(items = state.relatedTitles) { relatedTitle ->
            RelatedTitleItem(
                relatedTitle = relatedTitle,
                onClick = onTitleClick
            )
        }
    }
}