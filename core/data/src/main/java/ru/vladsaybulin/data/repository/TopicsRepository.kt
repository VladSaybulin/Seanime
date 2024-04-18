package ru.vladsaybulin.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.serialization.json.Json
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.data.model.asPOJO
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.database.ShikiDatabase
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.database.models.user.UserEntity
import ru.vladsaybulin.database.models.topic.PopulatedTopic
import ru.vladsaybulin.database.models.topic.TopicEntity
import ru.vladsaybulin.database.models.topic.asExternalModel
import ru.vladsaybulin.model.topic.Topic
import ru.vladsaybulin.model.topic.TopicLinkedType
import ru.vladsaybulin.network.datasource.TopicsDataSource
import ru.vladsaybulin.network.models.TopicDto
import ru.vladsaybulin.network.models.decodeLinkedAnime
import ru.vladsaybulin.network.models.decodeLinkedManga
import javax.inject.Inject

class TopicsRepository @Inject constructor(
    private val topicsDataSource: TopicsDataSource,
    private val database: ShikiDatabase,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val json: Json,
) {
    fun getNewsTopics(): Flow<List<Topic>> = database.topicsDao.getNewsTopic()
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

            val topicDbo = topic.asPOJO(
                linkedAnimeId = if (topic.hasLinked() && topic.linkedIsAnime()) {
                    topic.decodeLinkedAnime(json)
                        .also { anime.add(it.asEntity()) }
                        .id
                } else null,
                linkedMangaId = if (topic.hasLinked() && topic.linkedIsManga()) {
                    topic.decodeLinkedManga(json)
                        .also { manga.add(it.asPOJO()) }
                        .id
                } else null
            )
            users.add(topic.user.asPOJO())
            topics.add(topicDbo)
        }

        database.withTransaction {
            if (anime.isNotEmpty()) {
                database.animeDao.insertOrReplaceAnimes(anime)
            }
            if (manga.isNotEmpty()) {
                database.mangaDao.insertOrReplaceMangas(manga)
            }
            if (users.isNotEmpty()) {
                database.usersDao.insertOrReplaceUserEntities(users)
            }
            if (topics.isNotEmpty()) {
                database.topicsDao.run {
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