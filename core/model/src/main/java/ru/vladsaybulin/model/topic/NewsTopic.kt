package ru.vladsaybulin.model.topic

import kotlinx.datetime.Instant
import ru.vladsaybulin.model.user.BriefUser

class NewsTopic(
    val title: String,
    val imageUrl: String?,
    val createdAt: Instant,
    val commentsCount: Int,
    val user: BriefUser
)