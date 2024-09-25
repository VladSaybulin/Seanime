package ru.vladsaybulin.core.domain.home

import ru.vladsaybulin.data.repository.AnimeRepository
import javax.inject.Inject

class GetOngoingAnimesStreamUseCase @Inject constructor(private val animeRepository: AnimeRepository) {
    operator fun invoke(limit: Int = DefaultOngoingAnimesLimit) = animeRepository.getOngoingAnimesStream(limit)
}

internal const val DefaultOngoingAnimesLimit = 20