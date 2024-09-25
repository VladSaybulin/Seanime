package ru.vladsaybulin.core.domain.home

import ru.vladsaybulin.data.repository.LastRequestRepository
import ru.vladsaybulin.data.repository.TopicsRepository
import ru.vladsaybulin.model.request.Request
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours

class UpdateNewsTopicsUseCase @Inject constructor(
    private val topicsRepository: TopicsRepository,
    private val lastRequestRepository: LastRequestRepository
) {
    suspend operator fun invoke(forceRefresh: Boolean = true) {
        if (!forceRefresh && !isExpired()) return

        topicsRepository.refreshNewsTopics()
        lastRequestRepository.updateLastRequest(Request.NEWS, 0)
    }

    private suspend fun isExpired() = lastRequestRepository.isRequestExpired(
        request = Request.NEWS,
        targetId = 0,
        ttl = NewsTTL
    )

}

private val NewsTTL = 2.hours