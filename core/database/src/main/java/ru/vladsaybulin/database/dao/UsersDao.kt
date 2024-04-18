package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.vladsaybulin.database.models.user.UserEntity

@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceUserEntities(users: List<UserEntity>)

}