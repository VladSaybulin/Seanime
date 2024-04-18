package ru.vladsaybulin.model.user

import kotlinx.datetime.Instant

data class BriefUser(
    val id: Long,
    val nickname: String,
    val avatarUrl: String,
    val image: UserImage,
    val lastOnlineAt: Instant,
    val url: String
)