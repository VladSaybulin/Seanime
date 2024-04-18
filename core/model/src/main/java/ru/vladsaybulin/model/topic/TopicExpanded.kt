package ru.vladsaybulin.model.topic

import kotlinx.datetime.Instant
import ru.vladsaybulin.model.forum.Forum
import ru.vladsaybulin.model.user.BriefUser

data class TopicExpanded(
    val id: Long,
    val title: String,
    val body: String,
    val createdAt: Instant,
    val commentCount: String,
    val forum: Forum,
    val user: BriefUser,
    val type: TopicType,
    val linkedType: TopicLinkedType?,
    val linkedId: Long,
    val linked: Any,
    val viewed: Boolean,
    val lastCommentViewed: Boolean,
)