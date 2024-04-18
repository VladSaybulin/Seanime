package ru.vladsaybulin.core.domain

import ru.vladsaybulin.data.repository.FiltersRepository
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.search.Filters
import javax.inject.Inject

class GetFiltersUseCase @Inject constructor(
    private val filtersRepository: FiltersRepository
) {
    suspend operator fun invoke(
        entryType: EntryType,
        statusEnabled: Boolean = true,
        studioEnabled: Boolean = true,
        publisherEnabled: Boolean = true,
        genreEnabled: Boolean = true
    ): Filters = when (entryType) {
        EntryType.Anime -> filtersRepository.getAnimeFilters()
        EntryType.Manga -> filtersRepository.getMangaFilters()
    }.let {
        if (statusEnabled && studioEnabled && publisherEnabled && genreEnabled) it
        else it.copy(
            statusOptions = if (statusEnabled) it.statusOptions else null,
            studiosOptions = if (studioEnabled) it.studiosOptions else null,
            publishersOptions = if (publisherEnabled) it.publishersOptions else null,
            genresOption = if (statusEnabled) it.genresOption else null,
            themesOptions = if (statusEnabled) it.genresOption else null,
            demographicOptions = if (statusEnabled) it.genresOption else null
        )
    }
}