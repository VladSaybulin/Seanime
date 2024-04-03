package ru.vladsaybulin.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.data.model.asDbo
import ru.vladsaybulin.data.model.asDto
import ru.vladsaybulin.data.model.asUserRate
import ru.vladsaybulin.database.ShikiDatabase
import ru.vladsaybulin.model.UserRate
import ru.vladsaybulin.model.UserRateValues
import ru.vladsaybulin.network.datasource.UserRateDataSource
import javax.inject.Inject

class UserRateRepository @Inject constructor(
    private val userRateDataSource: UserRateDataSource,
    private val database: ShikiDatabase,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {
    fun getAnimeUserRate(animeId: Long): Flow<UserRate?> = flow {
        emit(userRateDataSource.getAnimeUserRate(animeId)?.asUserRate())
    }.flowOn(ioDispatcher)

    suspend fun updateUserRate(userRateId: Long, userRateValues: UserRateValues) {
        withContext(ioDispatcher) {
            val response = userRateDataSource.updateUserRate(userRateId, userRateValues.asDto())
            if (response != null) {
                database.userRateDao.insertOrReplaceUserRate(response.asDbo())
            }
        }
    }

    suspend fun deleteUserRate(userRateId: Long) {
        withContext(ioDispatcher) {

        }
    }
}