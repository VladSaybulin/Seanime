package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.database.models.topic.PopulatedTopicDbo
import ru.vladsaybulin.database.models.topic.TopicDbo

@Dao
interface TopicsDao {

    @Transaction
    @Query("SELECT * FROM topics WHERE forum = 'news' ORDER BY created_at DESC")
    fun getNewsTopic(): Flow<List<PopulatedTopicDbo>>

    @Insert
    fun insertTopicEntities(topics: List<TopicDbo>)

    @Query("DELETE FROM topics")
    fun deleteAll()
}