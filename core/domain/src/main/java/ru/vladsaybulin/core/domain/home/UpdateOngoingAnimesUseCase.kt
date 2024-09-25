package ru.vladsaybulin.core.domain.home

import ru.vladsaybulin.data.repository.AnimeRepository
import ru.vladsaybulin.data.repository.LastRequestRepository
import ru.vladsaybulin.model.request.Request
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours

class UpdateOngoingAnimesUseCase @Inject constructor(
    private val animeRepository: AnimeRepository,
    private val lastRequestRepository: LastRequestRepository,
) {
    suspend operator fun invoke(
        forceRefresh: Boolean,
        limit: Int = DefaultOngoingAnimesLimit
    ) {
        if (!forceRefresh && !isExpired()) return

        animeRepository.refreshOngoingAnimes(limit)
        lastRequestRepository.updateLastRequest(Request.ANIME_ONGOINGS, 0)
    }

    private suspend fun isExpired() = lastRequestRepository.isRequestExpired(
        request = Request.ANIME_ONGOINGS,
        targetId = 0,
        ttl = OngoingAnimesRequestTTL
    )
}

private val OngoingAnimesRequestTTL = 24.hours