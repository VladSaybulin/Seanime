package ru.vladsaybulin.data.repository

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ru.vladsaybulin.database.dao.LastRequestDao
import ru.vladsaybulin.database.models.lastrequest.LastRequestEntity
import ru.vladsaybulin.model.request.Request
import javax.inject.Inject
import kotlin.time.Duration

class LastRequestRepository @Inject constructor(private val lastRequestDao: LastRequestDao) {

    suspend fun isRequestExpired(request: Request, targetId: Long, ttl: Duration): Boolean {
        val lastRequestDate = lastRequestDao.getLastRequestDate(request, targetId) ?: return true
        return (lastRequestDate + ttl) < Clock.System.now()
    }

    suspend fun updateLastRequest(
        request: Request,
        targetId: Long,
        requestDate: Instant = Clock.System.now()
    ) {
        lastRequestDao.insertOrReplaceLastRequestDate(
            LastRequestEntity(
                request = request,
                targetId = targetId,
                requestDate = requestDate
            )
        )
    }

}