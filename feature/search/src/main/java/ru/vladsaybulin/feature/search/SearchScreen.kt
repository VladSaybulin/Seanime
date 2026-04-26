package ru.vladsaybulin.feature.search

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Badge
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import ru.vladsaybulin.core.designsystem.components.SeanimeInformation
import ru.vladsaybulin.core.designsystem.components.SeanimeSearchField
import ru.vladsaybulin.core.designsystem.components.ShikimoriDropdownChip
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.ui.LocalScreenContentPadding
import ru.vladsaybulin.core.ui.filters.AppliedFilters
import ru.vladsaybulin.core.ui.filters.FiltersBottomSheet
import ru.vladsaybulin.core.ui.filters.OptionValue
import ru.vladsaybulin.core.ui.filters.rememberFiltersState
import ru.vladsaybulin.core.ui.paging.PagingBox
import ru.vladsaybulin.core.ui.strings.orderString
import ru.vladsaybulin.core.ui2.strings.compose.LocalTitleStrings
import ru.vladsaybulin.core.ui2.strings.compose.ProvideTitleStringsByType
import ru.vladsaybulin.feature.search.navigation.SearchNavEvents
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.search.Order
import ru.vladsaybulin.model.search.SearchType
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.core.ui2.entry.EntryGrid
import ru.vladsaybulin.core.ui2.entry.EntryList
import ru.vladsaybulin.core.ui2.entry.anime.animeItems
import ru.vladsaybulin.core.ui2.entry.manga.mangaItems
import kotlin.math.max
import kotlin.math.min

@Composable
fun SearchScreen(
    navEvents: SearchNavEvents,
    viewModel: SearchViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val userRates by viewModel.allUserRateStatuses.collectAsStateWithLifecycle()

    SearchScreen(
        uiState = uiState,
        searchQuery = searchQuery,
        searchResultFlows = viewModel.searchResultFlows,
        onSearchTypeChanged = viewModel::onSearchTypeChanged,
        onOrderChanged = viewModel::onOrderChanged,
        onApplyFilters = viewModel::onApplyFilters,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onAnimeClick = { navEvents.navigateToAnime(it.id) },
        onMangaClick = { navEvents.navigateToManga(it.id) },
        userRates = userRates
    )
}

@Composable
private fun SearchScreen(
    uiState: SearchUiState,
    searchQuery: String,
    searchResultFlows: SearchResultFlows,
    userRates: Map<Long, UserRateStatus>,
    onSearchTypeChanged: (SearchType) -> Unit,
    onOrderChanged: (Order) -> Unit,
    onApplyFilters: (AppliedFilters) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onAnimeClick: (Anime) -> Unit,
    onMangaClick: (Manga) -> Unit
) {
    val titleType = when (uiState.selectedSearchType) {
        SearchType.Manga, SearchType.Ranobe -> EntryType.Manga
        else -> EntryType.Anime
    }
    ProvideTitleStringsByType(titleType) {
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
                SearchTopBar(
                    searchQuery = searchQuery,
                    title = uiState.title,
                    availableSearchTypes = uiState.availableSearchTypes,
                    selectedSearchType = uiState.selectedSearchType,
                    availableOrders = uiState.availableOrders,
                    selectedOrder = uiState.selectedOrder,
                    appliedFiltersCount = uiState.countActiveFilter(),
                    onSearchQueryChange = onSearchQueryChanged,
                    onSearchTypeChanged = onSearchTypeChanged,
                    onOrderChanged = onOrderChanged,
                    onFiltersClick = { showFilters = true }
                )

                SearchResult(
                    searchType = uiState.selectedSearchType,
                    pagingFlows = searchResultFlows,
                    onAnimeClick = onAnimeClick,
                    onMangaClick = onMangaClick,
                    userRates = userRates
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
}

@Composable
private fun SearchTopBar(
    searchQuery: String,
    title: SearchTitle,
    availableSearchTypes: List<SearchType>,
    selectedSearchType: SearchType,
    availableOrders: List<Order>,
    selectedOrder: Order,
    appliedFiltersCount: Int,
    onSearchQueryChange: (String) -> Unit,
    onSearchTypeChanged: (SearchType) -> Unit,
    onOrderChanged: (Order) -> Unit,
    onFiltersClick: () -> Unit,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = WindowInsets.statusBars,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .windowInsetsPadding(windowInsets),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SeanimeSearchField(
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            placeholder = { Text(text = searchTitleText(title)) }
        )
        Spacer(modifier = Modifier.height(4.dp))
        SearchOptions(
            availableSearchTypes = availableSearchTypes,
            selectedSearchType = selectedSearchType,
            availableOrders = availableOrders,
            selectedOrder = selectedOrder,
            appliedFiltersCount = appliedFiltersCount,
            onSearchTypeChanged = onSearchTypeChanged,
            onOrderChanged = onOrderChanged,
            onFiltersClick = onFiltersClick
        )
    }
}

@Composable
private fun SearchOptions(
    availableSearchTypes: List<SearchType>,
    selectedSearchType: SearchType,
    availableOrders: List<Order>,
    selectedOrder: Order,
    appliedFiltersCount: Int,
    onSearchTypeChanged: (SearchType) -> Unit,
    onOrderChanged: (Order) -> Unit,
    onFiltersClick: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .layout { measurable, constraints ->
                val width = max(constraints.minWidth, ControlPanelMinWidth.roundToPx())
                    .coerceAtMost(min(constraints.maxWidth, ControlPanelMaxWidth.roundToPx()))

                val placeable = measurable.measure(Constraints.fixedWidth(width))
                layout(width, placeable.height) {
                    placeable.placeRelative(0, 0)
                }
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val searchTypeText = @Composable { searchType: SearchType ->
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

            FiltersButton(
                appliedFiltersCount = appliedFiltersCount,
                onFiltersClick = onFiltersClick
            )

            ShikimoriDropdownChip(
                items = availableSearchTypes,
                onItemClick = onSearchTypeChanged,
                selected = true,
                selectedLabel = { searchTypeText(selectedSearchType) },
                itemLabel = searchTypeText,
                enabled = availableSearchTypes.size > 1
            )

            ShikimoriDropdownChip(
                items = availableOrders,
                onItemClick = onOrderChanged,
                selected = true,
                selectedLabel = { Text(text = orderString(selectedOrder)) },
                itemLabel = { Text(text = orderString(it)) }
            )
        }
    }
}

@Composable
private fun FiltersButton(
    appliedFiltersCount: Int,
    onFiltersClick: () -> Unit
) {
    FilterChip(
        selected = appliedFiltersCount > 0,
        onClick = onFiltersClick,
        label = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = SeanimeIcons.Tune,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                if (appliedFiltersCount != 0) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Badge { Text(text = appliedFiltersCount.toString()) }
                }
            }
        }
    )
}

@Composable
private fun SearchResult(
    searchType: SearchType,
    pagingFlows: SearchResultFlows,
    userRates: Map<Long, UserRateStatus>,
    onAnimeClick: (Anime) -> Unit,
    onMangaClick: (Manga) -> Unit
) {
    val animePagingItems = pagingFlows.animeSearchResult.collectAsLazyPagingItems()
    val mangaPagingItems = pagingFlows.mangaSearchResult.collectAsLazyPagingItems()
    val ranobePagingItems = pagingFlows.ranobeSearchResult.collectAsLazyPagingItems()

    PagingBox(
        pagingItems = when (searchType) {
            SearchType.Anime -> animePagingItems
            SearchType.Manga -> mangaPagingItems
            SearchType.Ranobe -> ranobePagingItems
        },
        emptyContent = { EmptySearchResult() }
    ) {
        EntryGrid {
            when (searchType) {
                SearchType.Anime -> animeItems(
                    animes = animePagingItems,
                    onItemClick = onAnimeClick,
                    userRateStatus = { userRates[it.id] ?: UserRateStatus.None }
                )
                SearchType.Manga -> mangaItems(
                    mangas = mangaPagingItems,
                    onItemClick = onMangaClick,
                    userRateStatus = { userRates[it.id] ?: UserRateStatus.None }
                )
                SearchType.Ranobe -> mangaItems(
                    mangas = ranobePagingItems,
                    onItemClick = onMangaClick,
                    userRateStatus = { userRates[it.id] ?: UserRateStatus.None }
                )
            }
        }
    }
}

@Composable
private fun EmptySearchResult() {
    SeanimeInformation(
        header = { Text(text = stringResource(id = R.string.feature_search_empty_result_header)) },
        description = { Text(text = stringResource(id = R.string.feature_search_empty_result_description)) }
    )
}

@Composable
@ReadOnlyComposable
private fun searchTitleText(title: SearchTitle) = when (title) {
    SearchTitle.Search -> stringResource(id = R.string.feature_search_title)
    is SearchTitle.Genre -> title.genreName
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

        EntryStatus.Released -> when (LocalTitleStrings.current.titleType) {
            EntryType.Anime -> R.string.feature_search_title_status_anime_releases
            EntryType.Manga -> R.string.feature_search_title_status_manga_releases
        }

        EntryStatus.Paused -> R.string.feature_search_title_status_paused

        EntryStatus.Discontinued -> R.string.feature_search_title_status_discontonued

        EntryStatus.None -> R.string.feature_search_title
    }
)

private fun SearchUiState.countActiveFilter() =
    appliedFilters.count { filter ->
        filter.value.any { option ->
            option.value != OptionValue.Unselected
        }
    }

private val ControlPanelMinWidth: Dp = 360.dp
private val ControlPanelMaxWidth: Dp = 720.dp
