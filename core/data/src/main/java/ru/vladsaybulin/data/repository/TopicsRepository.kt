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
import ru.vladsaybulin.data.model.asExternalModel
import ru.vladsaybulin.database.DatabaseTransactionRunner
import ru.vladsaybulin.database.dao.AnimeDao
import ru.vladsaybulin.database.dao.MangaDao
import ru.vladsaybulin.database.dao.TopicsDao
import ru.vladsaybulin.database.dao.UsersDao
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.database.models.topic.PopulatedTopic
import ru.vladsaybulin.database.models.topic.TopicEntity
import ru.vladsaybulin.database.models.topic.asExternalModel
import ru.vladsaybulin.database.models.user.UserEntity
import ru.vladsaybulin.model.topic.Topic
import ru.vladsaybulin.model.topic.TopicLinkedType
import ru.vladsaybulin.network.datasource.TopicsDataSource
import ru.vladsaybulin.network.models.TopicDto
import ru.vladsaybulin.network.models.decodeLinkedAnime
import ru.vladsaybulin.network.models.decodeLinkedManga
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
    fun getNewsTopics(): Flow<List<Topic>> = topicsDao.getNewsTopic()
        .onStart { refreshNewResources() }
        .map { topics -> topics.map(PopulatedTopic::asExternalModel) }
        .flowOn(ioDispatcher)


    private suspend fun refreshNewResources() {
        val freshTopics = topicsDataSource.getTopics(
            limit = 10,
            forumPermalink = "news"
        )

        val anime = mutableListOf<AnimeEntity>()
        val manga = mutableListOf<MangaEntity>()
        val users = mutableListOf<UserEntity>()
        val topics = mutableListOf<TopicEntity>()

        for (topic in freshTopics) {

            val topicDbo = topic.asEntity(
                linkedAnimeId = if (topic.hasLinked() && topic.linkedIsAnime()) {
                    topic.decodeLinkedAnime(json)
                        .also { anime.add(it.asEntity()) }
                        .id
                } else null,
                linkedMangaId = if (topic.hasLinked() && topic.linkedIsManga()) {
                    topic.decodeLinkedManga(json)
                        .also { manga.add(it.asEntity()) }
                        .id
                } else null
            )
            users.add(topic.user.asExternalModel())
            topics.add(topicDbo)
        }

        databaseTransactionRunner {
            if (anime.isNotEmpty()) {
                animeDao.insertOrReplaceAnimes(anime)
            }
            if (manga.isNotEmpty()) {
                mangaDao.insertOrReplaceMangas(manga)
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

private fun TopicDto.hasLinked() =
    linked != null

private fun TopicDto.linkedIsAnime() =
    linkedType == TopicLinkedType.Anime

private fun TopicDto.linkedIsManga() =
    linkedType == TopicLinkedType.Manga || linkedType == TopicLinkedType.Ranobe