package ru.vladsaybulin.core.domain

import ru.vladsaybulin.data.repository.RecentSearchQueryRepository
import javax.inject.Inject

class GetRecentSearchQueriesUseCase @Inject constructor(
    private val recentSearchQueryRepository: RecentSearchQueryRepository
) {
    operator fun invoke(limit: Int = 10) =
        recentSearchQueryRepository.getRecentSearchQuery(limit)

}