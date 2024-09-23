package ru.vladsaybulin.core.domain.search

import ru.vladsaybulin.data.repository.UserRateRepository
import ru.vladsaybulin.model.search.SearchType
import javax.inject.Inject

class GetAllUserRatesUseCase @Inject constructor(private val userRateRepository: UserRateRepository) {
    operator fun invoke(searchType: SearchType) = when (searchType) {
        SearchType.Anime -> userRateRepository.getAllAnimeUserRateStatusesStream()
        SearchType.Manga, SearchType.Ranobe -> userRateRepository.getAllMangaUserRateStatusesStream()
    }
}