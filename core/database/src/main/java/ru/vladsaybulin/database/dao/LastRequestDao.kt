package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.datetime.Instant
import ru.vladsaybulin.database.models.lastrequest.LastRequestEntity
import ru.vladsaybulin.model.request.Request

@Dao
interface LastRequestDao {

    @Query("SELECT date FROM last_requests WHERE target_id = :targetId AND type = :type")
    suspend fun getLastRequestDate(type: Request, targetId: Long): Instant?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceLastRequestDate(lastRequest: LastRequestEntity)
}