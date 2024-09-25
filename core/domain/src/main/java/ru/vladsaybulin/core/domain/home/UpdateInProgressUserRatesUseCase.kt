package ru.vladsaybulin.core.domain.home

import ru.vladsaybulin.data.repository.UserRateRepository
import ru.vladsaybulin.data.repository.UserRepository
import javax.inject.Inject

class UpdateInProgressUserRatesUseCase @Inject constructor(private val userRateRepository: UserRateRepository) {
    suspend operator fun invoke() {
        userRateRepository.refreshInProgressUserRates()
    }
}