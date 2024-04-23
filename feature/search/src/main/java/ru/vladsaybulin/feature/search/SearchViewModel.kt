package ru.vladsaybulin.feature.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.vladsaybulin.core.domain.GetRecentSearchQueriesUseCase
import ru.vladsaybulin.core.domain.GetSearchFiltersUseCase
import ru.vladsaybulin.core.domain.SearchAnimeUseCase
import ru.vladsaybulin.core.domain.SearchMangaUseCase
import ru.vladsaybulin.core.domain.SearchRanobeUseCase
import ru.vladsaybulin.core.navigation.args.SearchArgs
import ru.vladsaybulin.core.ui.filters.AppliedFilters
import ru.vladsaybulin.core.ui.filters.AppliedOptionValues
import ru.vladsaybulin.core.ui.filters.OptionValue
import ru.vladsaybulin.data.repository.FilterGenreRepository
import ru.vladsaybulin.data.repository.FilterPublisherRepository
import ru.vladsaybulin.data.repository.RecentSearchQueryRepository
import ru.vladsaybulin.data.repository.FilterStudioRepository
import ru.vladsaybulin.data.util.flowOf
import ru.vladsaybulin.feature.search.navigation.SearchArgs
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.search.FilterType
import ru.vladsaybulin.model.search.Order
import ru.vladsaybulin.model.search.QueryMapKey
import ru.vladsaybulin.model.search.SearchType
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getSearchFiltersUseCase: GetSearchFiltersUseCase,
    getRecentSearchQueriesUseCase: GetRecentSearchQueriesUseCase,
    searchAnimeUseCaseProvider: Provider<SearchAnimeUseCase>,
    searchMangaUseCaseProvider: Provider<SearchMangaUseCase>,
    searchRanobeUseCaseProvider: Provider<SearchRanobeUseCase>,
    private val filterStudioRepositoryProvider: Provider<FilterStudioRepository>,
    private val filterPublisherRepositoryProvider: Provider<FilterPublisherRepository>,
    private val filterGenreRepositoryProvider: Provider<FilterGenreRepository>,
    private val recentSearchQueryRepository: RecentSearchQueryRepository,
) : ViewModel() {

    private val args = SearchArgs(savedStateHandle)

    private val constAppliedFilters = buildMap {
        putAsSelectedIfNotNull(FilterType.Status, args.entryStatus?.serializedName)
        putAsSelectedIfNotNull(FilterType.Genre, args.genreId?.toString())
        putAsSelectedIfNotNull(FilterType.Demographic, args.demographicId?.toString())
        putAsSelectedIfNotNull(FilterType.Theme, args.themeId?.toString())
        putAsSelectedIfNotNull(FilterType.Studio, args.studioId?.toString())
        putAsSelectedIfNotNull(FilterType.Publisher, args.publisherId?.toString())
    }

    private var debouncedSearchJob: Job? = null

    private val availableSearchTypes = if (args.searchType != null) {
        persistentListOf(args.searchType!!)
    } else SearchType.entries.toImmutableList()

    private val availableOrders = Order.entries.toImmutableList()

    private val currentSearchType = MutableStateFlow(args.searchType ?: SearchType.Anime)
    private val currentOrder = MutableStateFlow(DefaultOrder)

    private val appliedFilters = MutableStateFlow<AppliedFilters>(emptyMap())

    private val filtersLoadingState = currentSearchType
        .map<SearchType, FiltersLoadingState> { searchType ->
            FiltersLoadingState.Success(
                getSearchFiltersUseCase(
                    searchType = searchType,
                    statusEnabled = args.entryStatus == null,
                    studioEnabled = args.studioId == null,
                    publisherEnabled = args.publisherId == null,
                    genreEnabled = args.genreId == null,
                    demographicEnabled = args.demographicId == null,
                    themesEnabled = args.themeId == null
                )
            )
        }
        .onStart { emit(FiltersLoadingState.Loading) }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val searchParams = MutableStateFlow(
        SearchParams(
            searchType = currentSearchType.value,
            order = currentOrder.value,
            searchQuery = _searchQuery.value,
            appliedFilters = appliedFilters.value
        )
    )

    val uiState = combine(
        currentSearchType,
        currentOrder,
        filtersLoadingState,
        appliedFilters,
        flowOf { args.getTitle() },
        ::createSearchUiState
    ).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        createSearchUiState(
            currentSearchType = currentSearchType.value,
            currentOrder = currentOrder.value,
            filtersLoadingState = FiltersLoadingState.Loading,
            appliedFilters = appliedFilters.value,
            title = SearchTitle.Search
        )
    )

    val searchResultFlows = SearchResultFlows(
        searchAnimeResult = searchParams.flatMapLatest { params ->
            if (params.searchType == SearchType.Anime) {
                searchAnimeUseCaseProvider.get().invoke(params.buildQueryMap())
            } else flowOf(PagingData.empty())
        }.cachedIn(viewModelScope),
        searchMangaResult = searchParams.flatMapLatest { params ->
            when (params.searchType) {
                SearchType.Manga -> searchMangaUseCaseProvider.get().invoke(params.buildQueryMap())
                SearchType.Ranobe -> searchRanobeUseCaseProvider.get()
                    .invoke(params.buildQueryMap())

                else -> flowOf(PagingData.empty())
            }.cachedIn(viewModelScope)
        }
    )

    val recentSearchQueriesState = getRecentSearchQueriesUseCase()
        .map { RecentSearchQueriesState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = RecentSearchQueriesState.Loading
        )

    fun onSearchTypeChanged(searchType: SearchType) {
        if (availableSearchTypes.size == 1) return
        currentSearchType.value = searchType
        appliedFilters.value = emptyMap()
        searchParams.update {
            it.copy(
                searchType = searchType,
                appliedFilters = emptyMap()
            )
        }
    }

    fun onOrderChanged(order: Order) {
        currentOrder.value = order
        searchParams.update { it.copy(order = order) }
    }

    fun onApplyFilters(map: AppliedFilters) {
        appliedFilters.value = map
        searchParams.update { it.copy(appliedFilters = map) }
    }

    fun onSearchQueryChanged(searchQuery: String) {
        _searchQuery.value = searchQuery
        debouncedSearchJob?.cancel()
        debouncedSearchJob = viewModelScope.launch {
            delay(DebounceSearchQueryMs)
            searchParams.emit(searchParams.value.copy(searchQuery = searchQuery))
        }.also { it.invokeOnCompletion { debouncedSearchJob = null } }
    }

    fun onSearchTriggered(searchQuery: String) {
        debouncedSearchJob?.cancel()
        _searchQuery.value = searchQuery
        viewModelScope.launch {
            recentSearchQueryRepository.insertOrReplaceRecentSearchQuery(searchQuery)
        }
        searchParams.update { it.copy(searchQuery = searchQuery) }
    }

    fun onDeleteRecentSearchQuery(query: String) {
        viewModelScope.launch {
            recentSearchQueryRepository.deleteRecentSearchQuery(query)
        }
    }

    private fun SearchParams.buildQueryMap() =
        buildMap {
            putFrom(appliedFilters)
            putFrom(constAppliedFilters)
            put(QueryMapKey.Search, searchQuery)
            put(QueryMapKey.Order, order.serializedValue)
        }

    private fun createSearchUiState(
        currentSearchType: SearchType,
        currentOrder: Order,
        filtersLoadingState: FiltersLoadingState,
        appliedFilters: AppliedFilters,
        title: SearchTitle
    ) = SearchUiState(
        currentSearchType = currentSearchType,
        currentOrder = currentOrder,
        filtersLoadingState = filtersLoadingState,
        appliedFilters = appliedFilters,
        availableSearchTypes = availableSearchTypes,
        availableOrders = availableOrders,
        title = title
    )

    private suspend fun SearchArgs.getTitle() = when {
        publisherId != null -> filterPublisherRepositoryProvider.get()
            .getFilterPublisherById(publisherId!!)
            ?.let { SearchTitle.Publisher(it.name) }

        studioId != null -> filterStudioRepositoryProvider.get()
            .getFilterStudioById(studioId!!)
            ?.let { SearchTitle.Studio(it.name) }

        genreId != null -> filterGenreRepositoryProvider.get()
            .getGenreById(searchType!!.toEntryType, genreId!!)
            ?.let { SearchTitle.Genre(it.russianName ?: it.englishName) }

        demographicId != null -> filterGenreRepositoryProvider.get()
            .getGenreById(searchType!!.toEntryType, demographicId!!)
            ?.let { SearchTitle.Demographic(it.russianName ?: it.englishName) }

        themeId != null -> filterGenreRepositoryProvider.get()
            .getGenreById(searchType!!.toEntryType, themeId!!)
            ?.let { SearchTitle.Theme(it.russianName ?: it.englishName) }

        entryStatus != null -> SearchTitle.Status(entryStatus!!)
        else -> null
    } ?: SearchTitle.Search
}

private data class SearchParams(
    val searchType: SearchType,
    val order: Order,
    val searchQuery: String,
    val appliedFilters: AppliedFilters,
)

fun MutableMap<QueryMapKey, String>.putFrom(appliedFilters: AppliedFilters) {
    appliedFilters.asSequence()
        .map { (filterType, optionValues) ->
            filterType.queryMapKey to optionValues.serializeOptions()
        }
        .groupBy(
            keySelector = { it.first },
            valueTransform = { it.second }
        )
        .mapValues { it.value.flatten().joinToString(separator = ",") }
        .filter { (_, serialized) -> serialized.isNotEmpty() }
        .also { putAll(it) }
}

fun AppliedOptionValues.serializeOptions() = mapNotNull { (serializedValue, optionValue) ->
    when (optionValue) {
        OptionValue.Selected -> serializedValue
        OptionValue.Excluded -> "!$serializedValue"
        OptionValue.Unselected -> null
    }
}

fun MutableMap<FilterType, AppliedOptionValues>.putAsSelectedIfNotNull(
    filterType: FilterType,
    optionValue: String?
) {
    if (optionValue == null) return
    this[filterType] = mapOf(optionValue to OptionValue.Selected)
}

val SearchType.toEntryType: EntryType
    get() = when (this) {
        SearchType.Anime -> EntryType.Anime
        SearchType.Manga, SearchType.Ranobe -> EntryType.Manga
    }

private const val DebounceSearchQueryMs = 500L
private val DefaultOrder = Order.Popularity