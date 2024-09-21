package ru.vladsaybulin.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.data.model.asFilterEntity
import ru.vladsaybulin.data.util.sync
import ru.vladsaybulin.database.dao.FiltersPublisherDao
import ru.vladsaybulin.database.models.filters.FilterPublisherEntity
import ru.vladsaybulin.database.models.filters.asExternalModel
import ru.vladsaybulin.datastore.SeanimePreferencesDataSource
import ru.vladsaybulin.model.manga.Publisher
import ru.vladsaybulin.network.datasource.PublisherDataSource
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

class FilterPublisherRepository @Inject constructor(
    private val publisherDataSource: PublisherDataSource,
    private val filtersPublisherDao: FiltersPublisherDao,
    private val seanimePreferencesDataSource: SeanimePreferencesDataSource,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getFilterPublisherById(publisherId: Long): Publisher? =
        withContext(ioDispatcher) {
            syncPublishers()
            filtersPublisherDao.getFilterPublisherById(publisherId)?.asExternalModel()
        }

    suspend fun getFilterPublishers(): List<Publisher> = withContext(ioDispatcher) {
        syncPublishers()
        filtersPublisherDao.getAllFilterPublishers()
            .map(FilterPublisherEntity::asExternalModel)
    }

    private suspend fun syncPublishers() {
        sync(
            ttl = PUBLISHERS_TTL,
            lastRequestDateFlow = seanimePreferencesDataSource.publishersLastRequestDate,
            updateLastRequest = seanimePreferencesDataSource::setLastPublishersRequestDate
        ) {
            val response = publisherDataSource.getPublishers()
            filtersPublisherDao.deleteAllFilterPublishers()
            filtersPublisherDao.insertOrIgnoreFilterPublishers(response.map { it.asFilterEntity() })
        }
    }
}

private val PUBLISHERS_TTL = 7.days