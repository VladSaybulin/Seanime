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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.data.TTLStrategies
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.data.model.linkedAnimeEntityShell
import ru.vladsaybulin.data.model.linkedMangaEntityShell
import ru.vladsaybulin.data.model.userEntityShell
import ru.vladsaybulin.data.request.RequestCoordinator
import ru.vladsaybulin.data.request.UpdateScope
import ru.vladsaybulin.data.request.cachedKey
import ru.vladsaybulin.database.dao.AnimeDao
import ru.vladsaybulin.database.dao.MangaDao
import ru.vladsaybulin.database.dao.TopicsDao
import ru.vladsaybulin.database.dao.UsersDao
import ru.vladsaybulin.database.models.lastrequest.RequestType
import ru.vladsaybulin.database.models.topic.PopulatedTopic
import ru.vladsaybulin.database.models.topic.asExternalModel
import ru.vladsaybulin.model.topic.Topic
import ru.vladsaybulin.network.datasource.TopicsDataSource
import javax.inject.Inject
import ru.vladsaybulin.core.domain.repository.TopicsRepository as DomainTopicsRepository

class TopicsRepository @Inject constructor(
    private val topicsDataSource: TopicsDataSource,
    private val topicsDao: TopicsDao,
    private val animeDao: AnimeDao,
    private val mangaDao: MangaDao,
    private val userDao: UsersDao,
    private val coordinator: RequestCoordinator,
    @param:Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val json: Json,
) : DomainTopicsRepository {
    override fun getNewsTopicsStream(): Flow<List<Topic>> = topicsDao.getNewsTopic()
        .map { topics -> topics.map(PopulatedTopic::asExternalModel) }
        .flowOn(ioDispatcher)

    override suspend fun refreshNewsTopics(force: Boolean) {
        coordinator.sync(
            key = cachedKey(RequestType.News),
            forceRefresh = force,
            ttlStrategy = TTLStrategies.News,
            block = { updateNewsTopics() }
        )
    }

    private suspend fun UpdateScope.updateNewsTopics() {
        val freshTopics = topicsDataSource.getTopics(
            limit = 10,
            forumPermalink = "news"
        )

        val anime = freshTopics.mapNotNull { it.linkedAnimeEntityShell(json) }
        val manga = freshTopics.mapNotNull { it.linkedMangaEntityShell(json) }
        val users = freshTopics.map { it.userEntityShell() }
        val topics = freshTopics.map { it.asEntity() }

        write {
            if (anime.isNotEmpty()) {
                animeDao.insertOrIgnoreAnimes(anime)
            }
            if (manga.isNotEmpty()) {
                mangaDao.insertOrIgnoreMangas(manga)
            }
            if (users.isNotEmpty()) {
                userDao.insertOrReplaceUserEntities(users)
            }
            if (topics.isNotEmpty()) {
                topicsDao.run {
                    deleteAll()
                    insertTopicEntities(topics)
                }
            }
        }
    }
}
