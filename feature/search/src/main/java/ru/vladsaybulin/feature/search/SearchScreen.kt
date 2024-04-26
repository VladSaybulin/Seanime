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
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import ru.vladsaybulin.core.designsystem.components.ShikimoriDropdownChip
import ru.vladsaybulin.core.designsystem.components.ShikimoriFilterChip
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.navigation.args.EntryDetailsArgs
import ru.vladsaybulin.core.navigation.args.asEntryDetailsArgs
import ru.vladsaybulin.core.ui.LocalScreenContentPadding
import ru.vladsaybulin.core.ui.anime.AnimeWithUserRateGrid
import ru.vladsaybulin.core.ui.filters.AppliedFilters
import ru.vladsaybulin.core.ui.filters.FiltersBottomSheet
import ru.vladsaybulin.core.ui.filters.OptionValue
import ru.vladsaybulin.core.ui.filters.rememberFiltersState
import ru.vladsaybulin.core.ui.manga.MangaWithUserRateGrid
import ru.vladsaybulin.core.ui.strings.LocalTargetStringsEntry
import ru.vladsaybulin.core.ui.strings.TargetStringsEntry
import ru.vladsaybulin.core.ui.strings.orderString
import ru.vladsaybulin.data.model.RecentSearchQuery
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.AnimeWithUserRate
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.manga.MangaWithUserRate
import ru.vladsaybulin.model.search.Order
import ru.vladsaybulin.model.search.SearchType

@Composable
fun SearchRoute(
    onEntryClick: (EntryDetailsArgs) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val recentSearchQueriesState by viewModel.recentSearchQueriesState.collectAsStateWithLifecycle()

    SearchScreen(
        uiState = uiState,
        searchQuery = searchQuery,
        recentSearchQueriesState = recentSearchQueriesState,
        searchResultFlows = viewModel.searchResultFlows,
        onEntryTypeChanged = viewModel::onSearchTypeChanged,
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
    uiState: SearchUiState,
    searchQuery: String,
    recentSearchQueriesState: RecentSearchQueriesState,
    searchResultFlows: SearchResultFlows,
    onEntryTypeChanged: (SearchType) -> Unit,
    onOrderChanged: (Order) -> Unit,
    onApplyFilters: (AppliedFilters) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit,
    onDeleteRecentSearchQuery: (String) -> Unit,
    onEntryClick: (EntryDetailsArgs) -> Unit
) {
    Box(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(LocalScreenContentPadding.current)
            .fillMaxSize()
    ) {

        var showFilters by remember { mutableStateOf(false) }

        val filtersLoadingState = uiState.filtersLoadingState
        val filtersState = if (filtersLoadingState is FiltersLoadingState.Success) {
            rememberFiltersState(
                filters = filtersLoadingState.filters,
                appliedFilters = uiState.appliedFilters
            )
        } else null

        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                val (active, setActive) = remember { mutableStateOf(false) }
                SearchSearchBar(
                    searchQuery = searchQuery,
                    active = active,
                    title = uiState.title,
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
                        SearchQuickResult(
                            searchType = uiState.currentSearchType,
                            pagingFlows = searchResultFlows,
                            onEntryClick = onEntryClick
                        )
                    }
                }
            }

            val appliedFiltersCount by remember {
                derivedStateOf {
                    uiState.appliedFilters.count { (_, filter) ->
                        filter.any { it.value != OptionValue.Unselected }
                    }
                }
            }

            SearchPanel(
                currentSearchType = uiState.currentSearchType,
                currentOrder = uiState.currentOrder,
                availableSearchTypes = uiState.availableSearchTypes,
                availableOrders = uiState.availableOrders,
                isFiltersLoading = filtersState == null,
                appliedFiltersCount = appliedFiltersCount,
                onEntryTypeChanged = onEntryTypeChanged,
                onOrderChanged = onOrderChanged,
                onFiltersClick = { showFilters = true }
            )

            SearchResult(
                searchType = uiState.currentSearchType,
                pagingFlows = searchResultFlows,
                onEntryClick = onEntryClick
            )
        }

        if (filtersState != null && showFilters) {
            FiltersBottomSheet(
                filtersState = filtersState,
                onDismissRequest = { showFilters = false },
                onApplyFilters = onApplyFilters
            )
        }
    }
}

@Composable
private fun SearchPanel(
    currentSearchType: SearchType,
    currentOrder: Order,
    availableSearchTypes: List<SearchType>,
    availableOrders: List<Order>,
    isFiltersLoading: Boolean,
    appliedFiltersCount: Int,
    onEntryTypeChanged: (SearchType) -> Unit,
    onOrderChanged: (Order) -> Unit,
    onFiltersClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .widthIn(min = ControlPanelMinWidth, max = ControlPanelMaxWidth)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FiltersButton(
                isFiltersLoading = isFiltersLoading,
                appliedFiltersCount = appliedFiltersCount,
                onFiltersClick = onFiltersClick
            )

            ShikimoriDropdownChip(
                items = availableSearchTypes,
                onItemClick = onEntryTypeChanged,
                selected = true,
                selectedLabel = { SearchTypeText(currentSearchType) },
                itemLabel = { SearchTypeText(it) },
                enabled = availableSearchTypes.size > 1
            )

            ShikimoriDropdownChip(
                items = availableOrders,
                onItemClick = onOrderChanged,
                selected = true,
                selectedLabel = { Text(text = orderString(currentOrder)) },
                itemLabel = { Text(text = orderString(it)) }
            )
        }
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
    title: SearchTitle,
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
                Text(
                    text = searchTitleText(title = title),
                    modifier = Modifier.alpha(0.6f)
                )
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
    searchType: SearchType,
    pagingFlows: SearchResultFlows,
    onEntryClick: (EntryDetailsArgs) -> Unit
) {
    when (searchType) {
        SearchType.Anime -> SearchAnimeResult(
            searchAnimeResult = pagingFlows.searchAnimeResult,
            onAnimeClick = { onEntryClick(it.asEntryDetailsArgs()) }
        )

        SearchType.Manga, SearchType.Ranobe -> SearchMangaResult(
            searchMangaResult = pagingFlows.searchMangaResult,
            onMangaClick = { onEntryClick(it.asEntryDetailsArgs()) }
        )
    }
}

@Composable
private fun SearchAnimeResult(
    searchAnimeResult: Flow<PagingData<AnimeWithUserRate>>,
    onAnimeClick: (Anime) -> Unit
) {
    val items = searchAnimeResult.collectAsLazyPagingItems()

    AnimeWithUserRateGrid(
        items = items,
        onEntryClick = { onAnimeClick(it.anime) },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun SearchMangaResult(
    searchMangaResult: Flow<PagingData<MangaWithUserRate>>,
    onMangaClick: (Manga) -> Unit
) {
    val items = searchMangaResult.collectAsLazyPagingItems()

    MangaWithUserRateGrid(
        items = items,
        onEntryClick = { onMangaClick(it.manga) },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun SearchQuickResult(
    searchType: SearchType,
    pagingFlows: SearchResultFlows,
    onEntryClick: (EntryDetailsArgs) -> Unit
) {
    when (searchType) {
        SearchType.Anime -> SearchAnimeQuickResult(
            searchAnimeResult = pagingFlows.searchAnimeResult,
            onAnimeClick = { onEntryClick(it.asEntryDetailsArgs()) }
        )

        SearchType.Manga, SearchType.Ranobe -> SearchMangaQuickResult(
            searchMangaResult = pagingFlows.searchMangaResult,
            onMangaClick = { onEntryClick(it.asEntryDetailsArgs()) }
        )
    }
}

@Composable
private fun SearchAnimeQuickResult(
    searchAnimeResult: Flow<PagingData<AnimeWithUserRate>>,
    onAnimeClick: (Anime) -> Unit
) {
    val pagingFlow = remember { searchAnimeResult.drop(1) }
    val items = pagingFlow.collectAsLazyPagingItems()
    if (items.itemCount > 0) {
        val itemSnapshotList = items.itemSnapshotList
        AnimeWithUserRateGrid(
            items = itemSnapshotList.subList(0, QuickSearchSize.coerceAtMost(itemSnapshotList.size)).filterNotNull(),
            onEntryClick = { onAnimeClick(it.anime) },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun SearchMangaQuickResult(
    searchMangaResult: Flow<PagingData<MangaWithUserRate>>,
    onMangaClick: (Manga) -> Unit
) {
    val items = searchMangaResult.collectAsLazyPagingItems()
    val subList = items.itemSnapshotList.subList(0, QuickSearchSize.coerceAtMost(items.itemCount))
        .filterNotNull()

    if (items.itemCount > 0) {
        MangaWithUserRateGrid(
            items = subList,
            onEntryClick = { onMangaClick(it.manga) },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun SearchTypeText(searchType: SearchType) {
    Text(
        text = stringResource(
            id = when (searchType) {
                SearchType.Anime -> R.string.feature_search_search_type_anime
                SearchType.Manga -> R.string.feature_search_search_type_manga
                SearchType.Ranobe -> R.string.feature_search_search_type_ranobe
            }
        )
    )
}

@Composable
@ReadOnlyComposable
private fun searchTitleText(title: SearchTitle) = when (title) {
    SearchTitle.Search -> stringResource(id = R.string.feature_search_title)
    is SearchTitle.Demographic -> title.demographicName
    is SearchTitle.Genre -> title.genreName
    is SearchTitle.Theme -> title.themeName
    is SearchTitle.Status -> statusTitleText(entryStatus = title.entryStatus)

    is SearchTitle.Publisher -> stringResource(
        id = R.string.feature_search_title_publisher,
        title.publisherName
    )

    is SearchTitle.Studio -> stringResource(
        id = R.string.feature_search_title_studio,
        title.studioName
    )
}


@Composable
@ReadOnlyComposable
private fun statusTitleText(entryStatus: EntryStatus) = stringResource(
    when (entryStatus) {
        EntryStatus.Anons -> R.string.feature_search_title_status_anonses
        EntryStatus.Ongoing -> R.string.feature_search_title_status_ongoings
        EntryStatus.Released -> if (LocalTargetStringsEntry.current == TargetStringsEntry.Anime) {
            R.string.feature_search_title_status_anime_releases
        } else {
            R.string.feature_search_title_status_manga_releases
        }

        EntryStatus.Paused -> R.string.feature_search_title_status_paused
        EntryStatus.Discontinued -> R.string.feature_search_title_status_discontonued
        EntryStatus.None -> R.string.feature_search_title
    }
)


private val QuickSearchSize = 20

private val ControlPanelMinWidth: Dp = 360.dp
private val ControlPanelMaxWidth: Dp = 720.dp
