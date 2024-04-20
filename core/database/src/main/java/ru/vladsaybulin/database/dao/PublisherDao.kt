package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.vladsaybulin.database.models.manga.PublisherEntity

@Dao
interface PublisherDao {

    @Query("SELECT * FROM publishers WHERE id = :publisherId")
    fun getPublisherById(publisherId: Long): PublisherEntity?

    @Query("SELECT * FROM publishers")
    fun getAllPublishers(): List<PublisherEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrIgnorePublisher(studioEntity: PublisherEntity)

    @Insert
    fun insertAllPublishers(studios: List<PublisherEntity>)

    @Query("DELETE FROM publishers")
    fun deleteAllPublishers()

}