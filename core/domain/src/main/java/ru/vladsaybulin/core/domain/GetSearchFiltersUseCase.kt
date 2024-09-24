package ru.vladsaybulin.core.domain

import ru.vladsaybulin.data.repository.FiltersRepository
import ru.vladsaybulin.model.search.Filters
import ru.vladsaybulin.model.search.SearchType
import javax.inject.Inject

class GetSearchFiltersUseCase @Inject constructor(
    private val filtersRepository: FiltersRepository
) {
    suspend operator fun invoke(
        searchType: SearchType,
        studioEnabled: Boolean = true,
        publisherEnabled: Boolean = true,
        genreEnabled: Boolean = true,
        themesEnabled: Boolean = true,
        demographicEnabled: Boolean = true
    ): Filters = when (searchType) {
        SearchType.Anime -> filtersRepository.getAnimeFilters()
        SearchType.Manga -> filtersRepository.getMangaFilters()
        SearchType.Ranobe -> filtersRepository.getRanobeFilters()
    }.let {
        if (studioEnabled && publisherEnabled && genreEnabled && themesEnabled && demographicEnabled) it
        else it.copy(
            studiosOptions = if (studioEnabled) it.studiosOptions else null,
            publishersOptions = if (publisherEnabled) it.publishersOptions else null,
            genresOption = if (genreEnabled) it.genresOption else null,
            themesOptions = if (themesEnabled) it.genresOption else null,
            demographicOptions = if (demographicEnabled) it.genresOption else null
        )
    }
}