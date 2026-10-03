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
import ru.vladsaybulin.core.domain.repository.FilterGenreRepository as DomainFilterGenreRepository
import ru.vladsaybulin.data.model.asFilterEntity
import ru.vladsaybulin.data.util.sync
import ru.vladsaybulin.database.dao.FilterGenreDao
import ru.vladsaybulin.database.models.filters.FilterGenreEntity
import ru.vladsaybulin.database.models.filters.asExternalModel
import ru.vladsaybulin.datastore.SeanimePreferencesDataSource
import ru.vladsaybulin.model.title.TitleType
import ru.vladsaybulin.model.genre.Genre
import ru.vladsaybulin.model.genre.GenreKind
import ru.vladsaybulin.network.datasource.GenreDataSource
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

class FilterGenreRepository @Inject constructor(
    private val genreDataSource: GenreDataSource,
    private val filtersGenreDao: FilterGenreDao,
    private val seanimePreferencesDataSource: SeanimePreferencesDataSource,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : DomainFilterGenreRepository {
    override suspend fun getGenreById(titleType: TitleType, genreId: Long): Genre? =
        withContext(ioDispatcher) {
            syncGenres(titleType)
            filtersGenreDao.getFilterGenreById(genreId)?.asExternalModel()
        }

    override suspend fun getGenres(titleType: TitleType, genreKind: GenreKind): List<Genre> =
        withContext(ioDispatcher) {
            syncGenres(titleType)
            filtersGenreDao.getFilterGenresByKind(titleType, genreKind)
                .map(FilterGenreEntity::asExternalModel)
        }


    private suspend fun syncGenres(titleType: TitleType) {
        sync(
            ttl = GENRES_TTL,
            lastRequestDateFlow = when (titleType) {
                TitleType.Anime -> seanimePreferencesDataSource.animeGenresLastRequestDate
                TitleType.Manga -> seanimePreferencesDataSource.mangaGenresLastRequestDate
            },
            updateLastRequest = when (titleType) {
                TitleType.Anime -> seanimePreferencesDataSource::setLastAnimeGenresRequestDate
                TitleType.Manga -> seanimePreferencesDataSource::setLastMangaGenresRequestDate
            }
        ) {
            val response = genreDataSource.getGenres(titleType)

            filtersGenreDao.deleteFilterGenresByEntryType(titleType)
            filtersGenreDao.insertOrIgnoreFilterGenres(response.map { it.asFilterEntity() })
        }
    }
}

private val GENRES_TTL = 10.days