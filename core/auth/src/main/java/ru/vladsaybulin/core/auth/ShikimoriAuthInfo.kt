package ru.vladsaybulin.core.auth

data class ShikimoriAuthInfo(
    val clientId: String,
    val clientSecret: String,
    val scope: String,
    val redirectUri: String,
    val userAgent: String
)