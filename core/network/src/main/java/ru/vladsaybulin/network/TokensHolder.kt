package ru.vladsaybulin.network

interface TokensHolder {
    suspend fun getAccessToken(): String?

    suspend fun refreshAccessToken(): String?
}