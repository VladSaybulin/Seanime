package ru.vladsaybulin.model.topic

import kotlinx.datetime.Instant
import ru.vladsaybulin.model.user.BriefUser

data class NewsResource(
    val id: Long,
    val title: String,
    val headerImageUrl: String?,
    val createdAt: Instant,
    val commentsCount: Int,
    val author: BriefUser
)