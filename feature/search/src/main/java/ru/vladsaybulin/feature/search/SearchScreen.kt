package ru.vladsaybulin.feature.search

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Badge
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import ru.vladsaybulin.core.designsystem.components.ShikimoriDropdownChip
import ru.vladsaybulin.core.designsystem.components.ShikimoriFilterChip
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.core.ui.anime.AnimeWithUserRateGrid
import ru.vladsaybulin.core.ui.filters.AppliedFilters
import ru.vladsaybulin.core.ui.filters.FiltersBottomSheet
import ru.vladsaybulin.core.ui.filters.OptionValue
import ru.vladsaybulin.core.ui.filters.rememberFiltersState
import ru.vladsaybulin.core.ui.manga.MangaWithUserRateGrid
import ru.vladsaybulin.core.ui.strings.entryTypeString
import ru.vladsaybulin.core.ui.strings.orderString
import ru.vladsaybulin.data.model.RecentSearchQuery
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.Order

@Composable
fun SearchRoute(
    onEntryClick: (EntryType, Long) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {

    val controlPanelState by viewModel.controlPanelState.collectAsStateWithLifecycle()
    val filtersUiState by viewModel.filtersUiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val recentSearchQueriesState by viewModel.recentSearchQueriesState.collectAsStateWithLifecycle()
    val searchResultState by viewModel.searchResultState.collectAsStateWithLifecycle()

    SearchScreen(
        controlPanelState = controlPanelState,
        filtersUiState = filtersUiState,
        searchQuery = searchQuery,
        recentSearchQueriesState = recentSearchQueriesState,
        searchResultState = searchResultState,
        onEntryTypeChanged = viewModel::onEntryTypeChanged,
        onOrderChanged = viewModel::onOrderChanged,
        onApplyFilters = viewModel::onApplyFilters,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onSearchTriggered = viewModel::onSearchTriggered,
        onDeleteRecentSearchQuery = viewModel::onDeleteRecentSearchQuery,
        onEntryClick = onEntryClick
    )
}

@Composable
private fun SearchScreen(
    controlPanelState: SearchControlPanelUiState,
    filtersUiState: SearchFiltersUiState,
    searchQuery: String,
    recentSearchQueriesState: RecentSearchQueriesState,
    searchResultState: SearchResultState,
    onEntryTypeChanged: (EntryType) -> Unit,
    onOrderChanged: (Order) -> Unit,
    onApplyFilters: (AppliedFilters) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit,
    onDeleteRecentSearchQuery: (String) -> Unit,
    onEntryClick: (EntryType, Long) -> Unit
) {
    var showFilters by remember { mutableStateOf(false) }

    val successFiltersUiState = when (filtersUiState) {
        SearchFiltersUiState.Loading -> null
        is SearchFiltersUiState.Success -> filtersUiState
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            val (active, setActive) = remember { mutableStateOf(false) }
            SearchSearchBar(
                searchQuery = searchQuery,
                active = active,
                onSearchQueryChanged = onSearchQueryChanged,
                onSearchTriggered = onSearchTriggered,
                onActiveChanged = setActive
            ) {
                if (searchQuery.isEmpty()) {
                    RecentSearchQueries(
                        recentSearchQueriesState = recentSearchQueriesState,
                        onSearchTriggered = {
                            onSearchTriggered(it)
                            setActive(false)
                        },
                        onDeleteRecentSearchQuery = onDeleteRecentSearchQuery
                    )
                } else {
                    /* TODO */
                }
            }
        }

        val isFiltersLoading: Boolean = successFiltersUiState == null
        val appliedFiltersCount: Int = successFiltersUiState?.applied?.count { optionStates ->
            optionStates.value.any { it.value != OptionValue.Unselected }
        } ?: 0

        SearchPanel(
            panelState = controlPanelState,
            isFiltersLoading = isFiltersLoading,
            appliedFiltersCount = appliedFiltersCount,
            onEntryTypeChanged = onEntryTypeChanged,
            onOrderChanged = onOrderChanged,
            onFiltersClick = { showFilters = true }
        )

        SearchResult(
            state = searchResultState,
            onEntryClick = onEntryClick
        )
    }

    if (successFiltersUiState != null) {
        SearchFilters(
            filtersUiState = successFiltersUiState,
            showFilters = showFilters,
            onDismissRequest = { showFilters = false },
            onApplyFilters = onApplyFilters
        )
    }
}

@Composable
private fun SearchFilters(
    filtersUiState: SearchFiltersUiState.Success,
    showFilters: Boolean,
    onDismissRequest: () -> Unit,
    onApplyFilters: (AppliedFilters) -> Unit,
) {
    val filtersState = rememberFiltersState(
        filters = filtersUiState.filters,
        appliedFilters = filtersUiState.applied
    )

    if (showFilters) {
        FiltersBottomSheet(
            filtersState = filtersState,
            onDismissRequest = onDismissRequest,
            onApplyFilters = onApplyFilters
        )
    }
}

@Composable
private fun SearchPanel(
    panelState: SearchControlPanelUiState,
    isFiltersLoading: Boolean,
    appliedFiltersCount: Int,
    onEntryTypeChanged: (EntryType) -> Unit,
    onOrderChanged: (Order) -> Unit,
    onFiltersClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .horizontalScroll(scrollState)
            .widthIn(max = ControlPanelMaxWidth),
        horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FiltersButton(
            isFiltersLoading = isFiltersLoading,
            appliedFiltersCount = appliedFiltersCount,
            onFiltersClick = onFiltersClick
        )

        ShikimoriDropdownChip(
            items = panelState.availableEntryTypes,
            onItemClick = onEntryTypeChanged,
            selected = true,
            selectedLabel = { Text(text = entryTypeString(panelState.currentEntryType)) },
            itemLabel = { Text(text = entryTypeString(it)) },
            enabled = !panelState.isEntryTypeLocked
        )

        ShikimoriDropdownChip(
            items = panelState.availableOrders,
            onItemClick = onOrderChanged,
            selected = true,
            selectedLabel = { Text(text = orderString(panelState.currentOrder)) },
            itemLabel = { Text(text = orderString(it)) }
        )
    }
}

@Composable
private fun FiltersButton(
    isFiltersLoading: Boolean,
    appliedFiltersCount: Int,
    onFiltersClick: () -> Unit
) {
    ShikimoriFilterChip(
        selected = appliedFiltersCount > 0,
        onClick = onFiltersClick,
        label = {
            Box(
                modifier = Modifier.animateContentSize()
            ) {
                if (isFiltersLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = ShikimoriIcons.Tune,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )

                        if (appliedFiltersCount > 0) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Badge {
                                Text(text = appliedFiltersCount.toString())
                            }
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchSearchBar(
    searchQuery: String,
    active: Boolean,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit,
    onActiveChanged: (Boolean) -> Unit,
    searchViewContent: @Composable ColumnScope.() -> Unit
) {
    BoxWithConstraints {
        SearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChanged,
            onSearch = {
                if (it.isNotBlank()) {
                    onSearchTriggered(it)
                }
                onActiveChanged(false)
            },
            active = active,
            onActiveChange = onActiveChanged,
            placeholder = {
                Text(text = stringResource(id = R.string.core_ui_search))
            },
            leadingIcon = {
                when {
                    active -> IconButton(onClick = { onActiveChanged(false) }) {
                        Icon(imageVector = ShikimoriIcons.ArrowBack, contentDescription = null)
                    }

                    else -> Icon(imageVector = ShikimoriIcons.Search, contentDescription = null)
                }
            },
            trailingIcon = if (searchQuery.isNotBlank()) {
                @Composable {
                    IconButton(onClick = { onSearchQueryChanged("") }) {
                        Icon(imageVector = ShikimoriIcons.Clear, contentDescription = null)
                    }
                }
            } else null,
            modifier = Modifier.widthIn(min = maxWidth - 32.dp, max = maxWidth),
            content = searchViewContent
        )
    }
}

@Composable
private fun RecentSearchQueries(
    recentSearchQueriesState: RecentSearchQueriesState,
    onSearchTriggered: (String) -> Unit,
    onDeleteRecentSearchQuery: (String) -> Unit
) {
    when (recentSearchQueriesState) {
        RecentSearchQueriesState.Loading -> SearchLoading()
        is RecentSearchQueriesState.Success -> RecentSearchQueriesContent(
            recentSearchQueriesState = recentSearchQueriesState,
            onSearchTriggered = onSearchTriggered,
            onDeleteRecentSearchQuery = onDeleteRecentSearchQuery
        )
    }
}

@Composable
private fun RecentSearchQueriesContent(
    recentSearchQueriesState: RecentSearchQueriesState.Success,
    onSearchTriggered: (String) -> Unit,
    onDeleteRecentSearchQuery: (String) -> Unit,
) {
    recentSearchQueriesState.recentQueries.forEach {
        RecentSearchQuery(
            recentSearchQuery = it,
            onTriggered = { onSearchTriggered(it.query) },
            onDelete = { onDeleteRecentSearchQuery(it.query) }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RecentSearchQuery(
    recentSearchQuery: RecentSearchQuery,
    onTriggered: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onTriggered,
                onLongClick = onDelete
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Icon(imageVector = ShikimoriIcons.History, contentDescription = null)
        Text(
            text = recentSearchQuery.query,
            modifier = Modifier.padding(start = 8.dp),
            style = ShikimoriTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun SearchLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun SearchResult(
    state: SearchResultState,
    onEntryClick: (EntryType, Long) -> Unit
) {
    when (state) {
        is SearchResultState.Animes -> AnimeSearchResult(
            state = state,
            onAnimeClick = { onEntryClick(EntryType.Anime, it) }
        )

        is SearchResultState.Mangas -> MangaSearchResult(
            state = state,
            onMangaClick = { onEntryClick(EntryType.Manga, it) })

        SearchResultState.None -> SearchLoading()
    }
}

@Composable
private fun AnimeSearchResult(
    state: SearchResultState.Animes,
    onAnimeClick: (Long) -> Unit
) {
    val items = state.pagingDataFlow.collectAsLazyPagingItems()

    AnimeWithUserRateGrid(
        items = items,
        onEntryClick = { onAnimeClick(it.anime.id) },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun MangaSearchResult(
    state: SearchResultState.Mangas,
    onMangaClick: (Long) -> Unit
) {
    val items = state.pagingDataFlow.collectAsLazyPagingItems()

    MangaWithUserRateGrid(
        items = items,
        onEntryClick = { onMangaClick(it.manga.id) },
        modifier = Modifier.fillMaxSize()
    )
}

private val ControlPanelMaxWidth: Dp = 720.dp
