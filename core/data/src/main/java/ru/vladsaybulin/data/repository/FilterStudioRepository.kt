package ru.vladsaybulin.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers
import ru.vladsaybulin.data.model.asFilterEntity
import ru.vladsaybulin.data.util.sync
import ru.vladsaybulin.database.dao.FilterStudioDao
import ru.vladsaybulin.database.models.filters.FilterStudioEntity
import ru.vladsaybulin.database.models.filters.asExternalModel
import ru.vladsaybulin.datastore.SeanimePreferencesDataSource
import ru.vladsaybulin.model.anime.Studio
import ru.vladsaybulin.network.datasource.StudioDataSource
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

class FilterStudioRepository @Inject constructor(
    private val studioDataSource: StudioDataSource,
    private val filtersStudioDao: FilterStudioDao,
    private val seanimePreferencesDataSource: SeanimePreferencesDataSource,
    @Dispatcher(ShikiDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getFilterStudioById(studioId: Long): Studio? =
        withContext(ioDispatcher) {
            syncStudios()
            filtersStudioDao.getFilterStudioById(studioId)?.asExternalModel()
        }
    suspend fun getFilterStudios() = withContext(ioDispatcher) {
        syncStudios()
        filtersStudioDao.getAllFilterStudios()
            .map(FilterStudioEntity::asExternalModel)
    }

    private suspend fun syncStudios() {
        sync(
            ttl = STUDIO_TTL,
            lastRequestDateFlow = seanimePreferencesDataSource.studiosLastRequestDate,
            updateLastRequest = seanimePreferencesDataSource::setLastStudiosRequestDate
        ) {
            val response = studioDataSource.getStudios()
            filtersStudioDao.deleteAllFilterStudios()
            filtersStudioDao.insertOrIgnoreFilterStudios(response.map { it.asFilterEntity() })
        }
    }
}

private val STUDIO_TTL = 7.days