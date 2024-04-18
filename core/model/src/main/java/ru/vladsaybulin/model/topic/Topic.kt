package ru.vladsaybulin.model.topic

import kotlinx.datetime.Instant
import ru.vladsaybulin.model.user.BriefUser

data class Topic(
    val id: Long,
    val title: String,
    val body: String?,
    val bodyHtml: String?,
    val footerHtml: String?,
    val createdAt: Instant,
    val commentsCount: Int,
    val forumPermalink: String,
    val user: BriefUser,
    val type: TopicType,
    val linkedType: TopicLinkedType,
    val linkedEntry: TopicLinkedEntry?,
    val viewed: Boolean,
    val lastCommentViewed: Boolean,
    val event: TopicEvent,
    val episode: Int?
)