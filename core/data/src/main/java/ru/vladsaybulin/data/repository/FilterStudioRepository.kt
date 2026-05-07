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
import ru.vladsaybulin.common.network.ShikiDispatchers
import ru.vladsaybulin.core.domain.repository.FilterStudioRepository as DomainFilterStudioRepository
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
) : DomainFilterStudioRepository {

    override suspend fun getFilterStudioById(studioId: Long): Studio? =
        withContext(ioDispatcher) {
            syncStudios()
            filtersStudioDao.getFilterStudioById(studioId)?.asExternalModel()
        }
    override suspend fun getFilterStudios(): List<Studio> = withContext(ioDispatcher) {
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