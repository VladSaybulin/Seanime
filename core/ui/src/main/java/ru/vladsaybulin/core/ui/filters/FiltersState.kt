package ru.vladsaybulin.core.ui.filters

import androidx.compose.runtime.Composable
import androidx.compose.runtime.asIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.util.fastForEach
import ru.vladsaybulin.model.AnimeKind
import ru.vladsaybulin.model.AnimeRating
import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.model.Genre
import ru.vladsaybulin.model.MangaKind
import ru.vladsaybulin.model.Publisher
import ru.vladsaybulin.model.Studio
import ru.vladsaybulin.model.UserRateStatus
import ru.vladsaybulin.model.search.Duration
import ru.vladsaybulin.model.search.FilterOption
import ru.vladsaybulin.model.search.FilterType
import ru.vladsaybulin.model.search.Filters
import ru.vladsaybulin.model.search.SeasonFilter

typealias FilterOptionStates <T> = List<FilterOptionState<T>>
typealias AppliedFiltersMap = Map<FilterType, Map<String, OptionValue>>

@Composable
fun rememberFiltersState(
    filters: Filters,
    appliedFilters: AppliedFiltersMap
): FiltersState {
    val state = remember(filters) {
        FiltersState(
            animeKindOptions = filters.animeKindOptions?.mapToStates(),
            mangaKindOptions = filters.mangaKindOptions?.mapToStates(),
            statusOptions = filters.statusOptions?.mapToStates(),
            myListStatusOptions = filters.myListStatus?.mapToStates(),
            durationOptions = filters.duration?.mapToStates(),
            seasonOptions = filters.seasonFilterOptions?.mapToStates(),
            ratingOptions = filters.ratingOptions?.mapToStates(),
            studioOptions = filters.studiosOptions?.mapToStates(),
            publisherOptions = filters.publishersOptions?.mapToStates(),
            genresOptions = filters.genresOption?.mapToStates(),
            themeOptions = filters.themesOptions?.mapToStates(),
            demographicOptions = filters.demographicOptions?.mapToStates(),
            initialScore = 0
        )
    }
    state.setAppliedFilters(appliedFilters)
    return state
}

class FiltersState(
    val animeKindOptions: FilterOptionStates<AnimeKind>?,
    val mangaKindOptions: FilterOptionStates<MangaKind>?,
    val statusOptions: FilterOptionStates<EntryStatus>?,
    val myListStatusOptions: FilterOptionStates<UserRateStatus>?,
    val durationOptions: FilterOptionStates<Duration>?,
    val seasonOptions: FilterOptionStates<SeasonFilter>?,
    val ratingOptions: FilterOptionStates<AnimeRating>?,
    val genresOptions: FilterOptionStates<Genre>?,
    val themeOptions: FilterOptionStates<Genre>?,
    val demographicOptions: FilterOptionStates<Genre>?,
    val studioOptions: FilterOptionStates<Studio>?,
    val publisherOptions: FilterOptionStates<Publisher>?,
    initialScore: Int
) {
    private var lastAppliedFilters: AppliedFilters = emptyMap()

    private var _selectedMinScoreState = mutableIntStateOf(initialScore)
    val selectedMinScoreState = _selectedMinScoreState.asIntState()

    val customSeasonOptions =
        mutableStateListOf<FilterOptionState<SeasonFilter>>()

    fun changeMinScore(newMinScore: Int) {
        _selectedMinScoreState.intValue = newMinScore
    }

    fun getAppliedFilters(): AppliedFilters = buildMap {
        val appliedKind = mutableMapOf<String, OptionValue>()

        if (!animeKindOptions.isNullOrEmpty()) {
            appliedKind.putAll(getAppliedFor(animeKindOptions))
        }

        if (!mangaKindOptions.isNullOrEmpty()) {
            appliedKind.putAll(getAppliedFor(mangaKindOptions))
        }

        put(FilterType.Kind, appliedKind)

        if (!statusOptions.isNullOrEmpty()) {
            put(FilterType.Status, getAppliedFor(statusOptions))
        }

        if (!myListStatusOptions.isNullOrEmpty()) {
            put(FilterType.MyListStatus, getAppliedFor(myListStatusOptions))
        }

        if (!durationOptions.isNullOrEmpty()) {
            put(FilterType.Duration, getAppliedFor(durationOptions))
        }

        if (!ratingOptions.isNullOrEmpty()) {
            put(FilterType.Rating, getAppliedFor(ratingOptions))
        }

        if (!studioOptions.isNullOrEmpty()) {
            put(FilterType.Studio, getAppliedFor(studioOptions))
        }

        if (!publisherOptions.isNullOrEmpty()) {
            put(FilterType.Kind, getAppliedFor(publisherOptions))
        }

        if (!seasonOptions.isNullOrEmpty()) {
            val appliedSeason = mutableMapOf<String, OptionValue>()
            appliedSeason.putAll(getAppliedFor(seasonOptions))
            appliedSeason.putAll(getAppliedFor(customSeasonOptions))
            put(FilterType.Season, appliedSeason)
        }

        val appliedGenres = mutableMapOf<String, OptionValue>()

        if (!genresOptions.isNullOrEmpty()) {
            appliedGenres.putAll(getAppliedFor(genresOptions))
        }

        if (!themeOptions.isNullOrEmpty()) {
            appliedGenres.putAll(getAppliedFor(themeOptions))
        }

        if (!demographicOptions.isNullOrEmpty()) {
            appliedGenres.putAll(getAppliedFor(demographicOptions))
        }

        put(FilterType.Genre, appliedGenres)

        if (selectedMinScoreState.intValue != 0) {
            put(
                FilterType.Score,
                mapOf(selectedMinScoreState.intValue.toString() to OptionValue.Selected)
            )
        }
    }

    fun setAppliedFilters(appliedFilters: AppliedFilters) {
        this.lastAppliedFilters = appliedFilters
        updateStates(appliedFilters)
    }

    fun cancelChanges() {
        updateStates(lastAppliedFilters)
    }

    fun resetAll() {
        updateStates(emptyMap())
    }

    private fun updateStates(appliedFilters: AppliedFilters) {

        val appliedKind = appliedFilters.kind()

        if (!animeKindOptions.isNullOrEmpty()) {
            updateFor(animeKindOptions, appliedKind)
        }

        if (!mangaKindOptions.isNullOrEmpty()) {
            updateFor(mangaKindOptions, appliedKind)
        }

        if (!statusOptions.isNullOrEmpty()) {
            updateFor(statusOptions, appliedFilters.status())
        }

        if (!myListStatusOptions.isNullOrEmpty()) {
            updateFor(myListStatusOptions, appliedFilters.myListStatus())
        }

        if (!durationOptions.isNullOrEmpty()) {
            updateFor(durationOptions, appliedFilters.duration())
        }

        if (!ratingOptions.isNullOrEmpty()) {
            updateFor(ratingOptions, appliedFilters.rating())
        }

        if (!studioOptions.isNullOrEmpty()) {
            updateFor(studioOptions, appliedFilters.studios())
        }

        if (!publisherOptions.isNullOrEmpty()) {
            updateFor(publisherOptions, appliedFilters.publishers())
        }

        if (!seasonOptions.isNullOrEmpty()) {
            val appliedSeason = appliedFilters.season()
            updateFor(seasonOptions, appliedSeason)
            updateFor(customSeasonOptions, appliedSeason)
        }

        val appliedGenres = appliedFilters.genres()

        if (!genresOptions.isNullOrEmpty()) {
            updateFor(genresOptions, appliedGenres)
        }

        if (!themeOptions.isNullOrEmpty()) {
            updateFor(themeOptions, appliedGenres)
        }

        if (!demographicOptions.isNullOrEmpty()) {
            updateFor(demographicOptions, appliedGenres)
        }

        _selectedMinScoreState.intValue = appliedFilters[FilterType.Score]?.entries
            ?.first { it.value == OptionValue.Selected }
            ?.key?.toInt() ?: 0
    }

    private fun getAppliedFor(options: FilterOptionStates<*>): AppliedOptionValues =
        options.filter { it.value != OptionValue.Unselected }
            .associate { it.option.serializedValue to it.value }

    private fun updateFor(
        options: FilterOptionStates<*>,
        appliedStates: Map<String, OptionValue>
    ) {
        options.fastForEach { option ->
            option.value = appliedStates[option.option.serializedValue]
                ?: OptionValue.Unselected
        }
    }
}

private fun <T> List<FilterOption<T>>.mapToStates() = map {
    FilterOptionState(option = it)
}