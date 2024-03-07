package ru.vladsaybulin.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.data.model.asUserRate
import ru.vladsaybulin.model.UserRate
import ru.vladsaybulin.network.datasource.UserRateDataSource
import javax.inject.Inject

class UserRateRepository @Inject constructor(
    private val userRateDataSource: UserRateDataSource,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {
    fun getAnimeUserRate(animeId: Long): Flow<UserRate?> = flow {
        emit(userRateDataSource.getAnimeUserRate(animeId)?.asUserRate())
    }.flowOn(ioDispatcher)
}