package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.database.models.topic.PopulatedTopic
import ru.vladsaybulin.database.models.topic.TopicEntity

@Dao
interface TopicsDao {

    @Transaction
    @Query("SELECT * FROM topics WHERE forum = 'news' ORDER BY created_at DESC")
    fun getNewsTopic(): Flow<List<PopulatedTopic>>

    @Insert
    fun insertTopicEntities(topics: List<TopicEntity>)

    @Query("DELETE FROM topics")
    fun deleteAll()
}