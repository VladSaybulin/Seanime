package ru.vladsaybulin.feature.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.vladsaybulin.core.domain.GetFiltersUseCase
import ru.vladsaybulin.core.domain.GetRecentSearchQueriesUseCase
import ru.vladsaybulin.core.ui.filters.AppliedFilters
import ru.vladsaybulin.core.ui.filters.AppliedOptionValues
import ru.vladsaybulin.core.ui.filters.OptionValue
import ru.vladsaybulin.data.repository.AnimeRepository
import ru.vladsaybulin.data.repository.MangaRepository
import ru.vladsaybulin.data.repository.RecentSearchQueryRepository
import ru.vladsaybulin.feature.search.navigation.SearchArgs
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.Order
import ru.vladsaybulin.model.search.FilterType
import ru.vladsaybulin.model.search.QueryMapKey
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getFiltersUseCase: GetFiltersUseCase,
    getRecentSearchQueriesUseCase: GetRecentSearchQueriesUseCase,
    private val recentSearchQueryRepository: RecentSearchQueryRepository,
    animeRepositoryProvider: Provider<AnimeRepository>,
    mangaRepositoryProvider: Provider<MangaRepository>
) : ViewModel() {

    private val args = SearchArgs(savedStateHandle)

    private val constAppliedFilters = buildMap {
        args.entryStatus?.let { put(FilterType.Status, selected(it.serializedName)) }
        args.genreId?.let { put(FilterType.Genre, selected(it.toString())) }
        args.studioId?.let { put(FilterType.Studio, selected(it.toString())) }
        args.publisherId?.let { put(FilterType.Publisher, selected(it.toString())) }
    }

    private var debouncedSearchJob: Job? = null

    private val isEntryTypeLocked = args.entryType != null

    private val _searchControlPanelUiState = MutableStateFlow(
        SearchControlPanelUiState(
            isEntryTypeLocked = isEntryTypeLocked,
            currentEntryType = args.entryType ?: EntryType.Anime,
            currentOrder = DefaultOrder
        )
    )
    val controlPanelState = _searchControlPanelUiState.asStateFlow()

    private val appliedFilters = MutableStateFlow(constAppliedFilters)

    private val filters = controlPanelState
        .map { it.currentEntryType }
        .distinctUntilChanged()
        .map { getFiltersUseCase(it) }

    val filtersUiState = (combine(
        filters,
        appliedFilters,
        transform = SearchFiltersUiState::Success
    ) as Flow<SearchFiltersUiState>)
        .onStart { emit(SearchFiltersUiState.Loading) }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = SearchFiltersUiState.Loading
        )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val recentSearchQueriesState = getRecentSearchQueriesUseCase()
        .map { RecentSearchQueriesState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = RecentSearchQueriesState.Loading
        )

    private val searchParams = MutableStateFlow(
        SearchParams(
            entryType = _searchControlPanelUiState.value.currentEntryType,
            order = _searchControlPanelUiState.value.currentOrder,
            searchQuery = _searchQuery.value,
            appliedFilters = appliedFilters.value
        )
    )

    val searchResultState = searchParams.map { params ->
        when (params.entryType) {
            EntryType.Anime -> animeRepositoryProvider.get()
                .getPagedAnime(params.buildQueryMap())
                .let { SearchResultState.Animes(it.cachedIn(viewModelScope)) }

            EntryType.Manga -> mangaRepositoryProvider.get()
                .getPagedManga(params.buildQueryMap())
                .let { SearchResultState.Mangas(it.cachedIn(viewModelScope)) }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = SearchResultState.None
    )

    fun onEntryTypeChanged(entryType: EntryType) {
        if (isEntryTypeLocked) return
        _searchControlPanelUiState.update { it.copy(currentEntryType = entryType) }
        appliedFilters.value = emptyMap()
        searchParams.update {
            it.copy(
                entryType = entryType,
                appliedFilters = emptyMap()
            )
        }
    }

    fun onOrderChanged(order: Order) {
        _searchControlPanelUiState.update { it.copy(currentOrder = order) }
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
        }
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
}

private data class SearchParams(
    val entryType: EntryType,
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

private fun selected(optionValue: String): Map<String, OptionValue> =
    mapOf(optionValue to OptionValue.Selected)

private const val DebounceSearchQueryMs = 500L
private val DefaultOrder = Order.Popularity