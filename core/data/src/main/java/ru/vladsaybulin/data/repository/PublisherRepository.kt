package ru.vladsaybulin.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.data.util.sync
import ru.vladsaybulin.database.dao.PublisherDao
import ru.vladsaybulin.database.models.manga.PublisherEntity
import ru.vladsaybulin.database.models.manga.asExternalModel
import ru.vladsaybulin.datastore.ShikiPreferencesDataSource
import ru.vladsaybulin.model.Publisher
import ru.vladsaybulin.network.datasource.PublisherDataSource
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

class PublisherRepository @Inject constructor(
    private val publisherDataSource: PublisherDataSource,
    private val publisherDao: PublisherDao,
    private val shikiPreferencesDataSource: ShikiPreferencesDataSource,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getPublishers(): List<Publisher> = withContext(ioDispatcher) {
        syncPublishers()
        publisherDao.getAllPublishers()
            .map(PublisherEntity::asExternalModel)
    }

    private suspend fun syncPublishers() {
        sync(
            ttl = PUBLISHERS_TTL,
            lastRequestDateFlow = shikiPreferencesDataSource.publishersLastRequestDate,
            updateLastRequest = shikiPreferencesDataSource::setLastPublishersRequestDate
        ) {
            val response = publisherDataSource.getPublishers()
            publisherDao.deleteAllPublishers()
            publisherDao.insertAllPublishers(response.map { it.asEntity() })
        }
    }
}

private val PUBLISHERS_TTL = 7.days