package ru.vladsaybulin.core.domain

import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetEnableAutocorrectUserRateUseCase @Inject constructor() {
    operator fun invoke() = flowOf(true)
}