package ru.vladsaybulin.database.models.topic

import androidx.room.Embedded
import androidx.room.Relation
import ru.vladsaybulin.database.models.UserDbo
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.anime.asExternalModel
import ru.vladsaybulin.database.models.asExternalModel
import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.database.models.manga.asExternalModel
import ru.vladsaybulin.model.topic.Topic
import ru.vladsaybulin.model.topic.TopicLinkedEntry

data class PopulatedTopicDbo(
    @Embedded
    val topicDbo: TopicDbo,

    @Relation(
        entity = UserDbo::class,
        parentColumn = "user_id",
        entityColumn = "id"
    )
    val userDbo: UserDbo,

    @Relation(
        entity = AnimeEntity::class,
        parentColumn = "anime_id",
        entityColumn = "id"
    )
    val animeDbo: AnimeEntity?,

    @Relation(
        entity = MangaEntity::class,
        parentColumn = "manga_id",
        entityColumn = "id"
    )
    val mangaDbo: MangaEntity?
)

fun PopulatedTopicDbo.asExternalModel() = Topic(
    id = topicDbo.id,
    title = topicDbo.title,
    body = topicDbo.body,
    bodyHtml = topicDbo.bodyHtml,
    footerHtml = topicDbo.footerHtml,
    createdAt = topicDbo.createdAt,
    commentsCount = topicDbo.commentsCount,
    forumPermalink = topicDbo.forumPermalink,
    user = userDbo.asExternalModel(),
    type = topicDbo.type,
    linkedType = topicDbo.linkedType,
    linkedEntry = TopicLinkedEntry(
        anime = animeDbo?.asExternalModel(),
        manga = mangaDbo?.asExternalModel(),
    ),
    viewed = topicDbo.viewed,
    lastCommentViewed = topicDbo.lastCommentViewed,
    event = topicDbo.event,
    episode = topicDbo.episode
)