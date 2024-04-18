package ru.vladsaybulin.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.data.util.sync
import ru.vladsaybulin.database.dao.StudioDao
import ru.vladsaybulin.database.models.anime.StudioEntity
import ru.vladsaybulin.database.models.anime.asExternalModel
import ru.vladsaybulin.datastore.ShikiPreferencesDataSource
import ru.vladsaybulin.network.datasource.StudioDataSource
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

class StudioRepository @Inject constructor(
    private val studioDataSource: StudioDataSource,
    private val studioDao: StudioDao,
    private val shikiPreferencesDataSource: ShikiPreferencesDataSource,
    @Dispatcher(ShikiDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getStudios() = withContext(ioDispatcher) {
        syncStudios()
        studioDao.getAllStudios()
            .map(StudioEntity::asExternalModel)
    }

    private suspend fun syncStudios() {
        sync(
            ttl = STUDIO_TTL,
            lastRequestDateFlow = shikiPreferencesDataSource.studiosLastRequestDate,
            updateLastRequest = shikiPreferencesDataSource::setLastStudiosRequestDate
        ) {
            val response = studioDataSource.getStudios()
            studioDao.deleteAllStudios()
            studioDao.insertAllStudios(response.map { it.asEntity() })
        }
    }

}

private val STUDIO_TTL = 7.days