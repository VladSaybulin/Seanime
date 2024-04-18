package ru.vladsaybulin.model.forum

data class Forum(
    val id: Long,
    val position: Int,
    val name: String,
    val permalink: String
)