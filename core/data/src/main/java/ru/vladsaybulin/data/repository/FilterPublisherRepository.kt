/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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