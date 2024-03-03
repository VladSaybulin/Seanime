package ru.vladsaybulin.network.common

interface TokensHolder {
    suspend fun getAccessToken(): String?

    suspend fun refreshAccessToken(): String?
}