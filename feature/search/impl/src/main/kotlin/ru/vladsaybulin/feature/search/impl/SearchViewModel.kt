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

package ru.vladsaybulin.feature.search.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.Lazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
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
import ru.vladsaybulin.core.domain.GetSearchFiltersUseCase
import ru.vladsaybulin.core.domain.repository.FilterGenreRepository
import ru.vladsaybulin.core.domain.repository.FilterPublisherRepository
import ru.vladsaybulin.core.domain.repository.FilterStudioRepository
import ru.vladsaybulin.core.domain.search.GetAllUserRatesUseCase
import ru.vladsaybulin.core.domain.search.GetPagedAnimeSearchUseCase
import ru.vladsaybulin.core.domain.search.GetPagedMangaSearchUseCase
import ru.vladsaybulin.core.ui.filters.AppliedFilters
import ru.vladsaybulin.core.ui.filters.AppliedOptionValues
import ru.vladsaybulin.core.ui.filters.OptionValue
import ru.vladsaybulin.feature.search.api.navigation.PresetSearchFilter
import ru.vladsaybulin.feature.search.api.navigation.SearchNavKey
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.search.FilterType
import ru.vladsaybulin.model.search.Order
import ru.vladsaybulin.model.search.QueryMapKey
import ru.vladsaybulin.model.search.SearchType
import javax.inject.Provider

@HiltViewModel(assistedFactory = SearchViewModel.Factory::class)
class SearchViewModel @AssistedInject constructor(
    getSearchFiltersUseCase: GetSearchFiltersUseCase,
    getPagedAnimeSearchUseCase: Lazy<GetPagedAnimeSearchUseCase>,
    getPagedMangaSearchUseCase: Lazy<GetPagedMangaSearchUseCase>,
    getAllUserRatesUseCase: GetAllUserRatesUseCase,
    private val filterStudioRepositoryProvider: Provider<FilterStudioRepository>,
    private val filterPublisherRepositoryProvider: Provider<FilterPublisherRepository>,
    private val filterGenreRepositoryProvider: Provider<FilterGenreRepository>,
    @Assisted private val key: SearchNavKey
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(key: SearchNavKey): SearchViewModel
    }

    private val constAppliedFilters = buildMap {
        key.presetSearchFilter?.let { (field, id) ->
            val filterType = when (field) {
                PresetSearchFilter.Field.Genre -> FilterType.Genre
                PresetSearchFilter.Field.Studio -> FilterType.Studio
                PresetSearchFilter.Field.Publisher -> FilterType.Publisher
            }

            putAsSelectedIfNotNull(filterType, id)
        }

        if (key.ongoing == true) {
            putAsSelectedIfNotNull(FilterType.Status, EntryStatus.Ongoing)
        }
    }

    private var debouncedSearchJob: Job? = null

    private val availableSearchTypes = key.type?.let { persistentListOf(it) }
        ?: SearchType.entries.toImmutableList()

    private val availableOrders = Order.entries.toImmutableList()

    private val currentSearchType = MutableStateFlow(key.type ?: DefaultSearchType)
    private val currentOrder = MutableStateFlow(DefaultOrder)

    private val appliedFilters: MutableStateFlow<AppliedFilters> = MutableStateFlow(emptyMap())

    private val filtersLoadingState = currentSearchType
        .map<SearchType, FiltersLoadingState> { searchType ->
            FiltersLoadingState.Success(
                getSearchFiltersUseCase(
                    searchType = searchType,
                    studioEnabled = key.presetSearchFilter?.field != PresetSearchFilter.Field.Studio,
                    publisherEnabled = key.presetSearchFilter?.field != PresetSearchFilter.Field.Publisher,
                    genreEnabled = true
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
        ::createSearchUiState
    ).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        SearchUiState(
            selectedSearchType = currentSearchType.value,
            selectedOrder = currentOrder.value,
            filtersLoadingState = FiltersLoadingState.Loading,
            appliedFilters = appliedFilters.value,
            availableSearchTypes = availableSearchTypes,
            availableOrders = availableOrders,
            title = SearchTitle.Search
        )
    )

    val searchResultFlows = SearchResultFlows(
        animeSearchResult = searchParams.flatMapLatest { params ->
            if (params.searchType == SearchType.Anime) {
                getPagedAnimeSearchUseCase.get().invoke(params.buildQueryMap())
            } else flowOf(PagingData.empty())
        }.cachedIn(viewModelScope),
        mangaSearchResult = searchParams.flatMapLatest { params ->
            if (params.searchType == SearchType.Manga) {
                getPagedMangaSearchUseCase.get().invoke(params.buildQueryMap(), false)
            } else flowOf(PagingData.empty())
        }.cachedIn(viewModelScope),
        ranobeSearchResult = searchParams.flatMapLatest { params ->
            if (params.searchType == SearchType.Ranobe) {
                getPagedMangaSearchUseCase.get().invoke(params.buildQueryMap(), true)
            } else flowOf(PagingData.empty())
        }.cachedIn(viewModelScope)
    )

    val allUserRateStatuses = currentSearchType.flatMapLatest { getAllUserRatesUseCase(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    fun onSearchTypeChanged(searchType: SearchType) {
        if (availableSearchTypes.size == 1) return
        currentSearchType.value = searchType
        appliedFilters.value = buildMap {
            if (key.ongoing == true) {
                putAsSelectedIfNotNull(FilterType.Status, EntryStatus.Ongoing)
            }
        }
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

    private fun SearchParams.buildQueryMap() =
        buildMap {
            putFrom(appliedFilters)
            putFrom(constAppliedFilters)
            put(QueryMapKey.Search, searchQuery)
            put(QueryMapKey.Order, order.serializedValue)
        }

    private suspend fun createSearchUiState(
        currentSearchType: SearchType,
        currentOrder: Order,
        filtersLoadingState: FiltersLoadingState,
        appliedFilters: AppliedFilters
    ) = SearchUiState(
        selectedSearchType = currentSearchType,
        selectedOrder = currentOrder,
        filtersLoadingState = filtersLoadingState,
        appliedFilters = appliedFilters,
        availableSearchTypes = availableSearchTypes,
        availableOrders = availableOrders,
        title = getTitle()
    )

    private suspend fun getTitle(): SearchTitle {
        if (key.ongoing == true) {
            return SearchTitle.Status(EntryStatus.Ongoing)
        }

        val preset = key.presetSearchFilter ?: return SearchTitle.Search
        return when(preset.field) {
            PresetSearchFilter.Field.Genre -> getGenreTitle(currentSearchType.value, preset.id.toLong())
            PresetSearchFilter.Field.Studio -> getStudioTitle(preset.id.toLong())
            PresetSearchFilter.Field.Publisher -> getPublisherTitle(preset.id.toLong())
        }
    }

    private suspend fun getStudioTitle(studioId: Long) =
        filterStudioRepositoryProvider.get()
            .getFilterStudioById(studioId)
            ?.let { SearchTitle.Studio(it.name) }
            ?: SearchTitle.Search

    private suspend fun getPublisherTitle(publisherId: Long) =
        filterPublisherRepositoryProvider.get()
            .getFilterPublisherById(publisherId)
            ?.let { SearchTitle.Studio(it.name) }
            ?: SearchTitle.Search

    private suspend fun getGenreTitle(
        searchType: SearchType,
        genreId: Long
    ): SearchTitle {
        val genre = filterGenreRepositoryProvider.get().getGenreById(searchType.entryType, genreId)
            ?: return SearchTitle.Search

        return SearchTitle.Genre(genre.russianName ?: genre.englishName, genre.kind)
    }
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
    optionValue: Any?
) {
    if (optionValue == null) return
    this[filterType] = mapOf(optionValue.toString() to OptionValue.Selected)
}

val SearchType.entryType: EntryType
    get() = when (this) {
        SearchType.Anime -> EntryType.Anime
        SearchType.Manga, SearchType.Ranobe -> EntryType.Manga
        else -> throw IllegalStateException("Can't give EntryType for SearchType.${this.name}")
    }

private const val DebounceSearchQueryMs = 500L

private val DefaultOrder = Order.Popularity
private val DefaultSearchType = SearchType.Anime