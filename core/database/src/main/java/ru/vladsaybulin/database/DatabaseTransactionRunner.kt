package ru.vladsaybulin.database

import androidx.room.withTransaction
import javax.inject.Inject

class DatabaseTransactionRunner @Inject constructor(
    private val database: SeanimeRoomDatabase
) {
    suspend operator fun <R> invoke(block: suspend () -> R): R {
        return database.withTransaction(block)
    }
}