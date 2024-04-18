package ru.vladsaybulin.database

import androidx.room.withTransaction
import javax.inject.Inject

class DatabaseTransactionRunner @Inject constructor(
    private val database: ShikiRoomDatabase
) {
    suspend operator fun invoke(block: suspend () -> Unit) {
        database.withTransaction(block)
    }
}