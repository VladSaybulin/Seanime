package ru.vladsaybulin.core.domain.home

import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.data.repository.UserRateRepository
import ru.vladsaybulin.model.userrate.UserRateWithEntry
import javax.inject.Inject

class GetInProgressUserRatesUseCase @Inject constructor(
    private val userRateRepository: UserRateRepository
) {
    operator fun invoke(limit: Int = DefaultInProgressUserRatesLimit): Flow<List<UserRateWithEntry>> =
        userRateRepository.getInProgressUserRatesStream(limit)
}

private const val DefaultInProgressUserRatesLimit = 10