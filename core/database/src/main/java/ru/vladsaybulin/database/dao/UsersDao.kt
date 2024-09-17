package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.database.models.user.UserEntity

@Dao
interface UsersDao {

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: Long) : Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceUserEntities(users: List<UserEntity>)

}