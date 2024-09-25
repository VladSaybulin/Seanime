package ru.vladsaybulin.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.serialization.json.Json
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.data.model.linkedAnimeEntityShell
import ru.vladsaybulin.data.model.linkedMangaEntityShell
import ru.vladsaybulin.data.model.userEntityShell
import ru.vladsaybulin.database.DatabaseTransactionRunner
import ru.vladsaybulin.database.dao.AnimeDao
import ru.vladsaybulin.database.dao.MangaDao
import ru.vladsaybulin.database.dao.TopicsDao
import ru.vladsaybulin.database.dao.UsersDao
import ru.vladsaybulin.database.models.topic.PopulatedTopic
import ru.vladsaybulin.database.models.topic.asExternalModel
import ru.vladsaybulin.model.topic.Topic
import ru.vladsaybulin.network.datasource.TopicsDataSource
import javax.inject.Inject

class TopicsRepository @Inject constructor(
    private val topicsDataSource: TopicsDataSource,
    private val topicsDao: TopicsDao,
    private val animeDao: AnimeDao,
    private val mangaDao: MangaDao,
    private val userDao: UsersDao,
    private val databaseTransactionRunner: DatabaseTransactionRunner,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val json: Json,
) {
    fun getNewsTopicsStream(): Flow<List<Topic>> = topicsDao.getNewsTopic()
        .map { topics -> topics.map(PopulatedTopic::asExternalModel) }
        .flowOn(ioDispatcher)

    suspend fun refreshNewsTopics() {
        val freshTopics = topicsDataSource.getTopics(
            limit = 10,
            forumPermalink = "news"
        )

        val anime = freshTopics.mapNotNull { it.linkedAnimeEntityShell(json) }
        val manga = freshTopics.mapNotNull { it.linkedMangaEntityShell(json) }
        val users = freshTopics.map { it.userEntityShell() }
        val topics = freshTopics.map { it.asEntity() }


        databaseTransactionRunner {
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
