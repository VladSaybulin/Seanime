package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.topic.TopicDbo
import ru.vladsaybulin.network.models.TopicDto

fun TopicDto.asEntity(
    linkedAnimeId: Long? = null,
    linkedMangaId: Long? = null
) = TopicDbo(
    id = id,
    title = title,
    body = body,
    bodyHtml = bodyHtml,
    footerHtml = footerHtml,
    createdAt = createdAt,
    commentsCount = commentsCount,
    forumPermalink = forum.permalink,
    userId = user.id,
    type = type,
    linkedType = linkedType,
    animeId = linkedAnimeId,
    mangaId = linkedMangaId,
    viewed = viewed,
    lastCommentViewed = lastCommentViewed,
    event = event,
    episode = episode
)
