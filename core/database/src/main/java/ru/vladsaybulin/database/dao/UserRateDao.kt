package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.vladsaybulin.database.models.UserRateDbo

@Dao
interface UserRateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceUserRates(userRates: List<UserRateDbo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceUserRate(userRate: UserRateDbo)

    @Query("DELETE FROM user_rates WHERE id = :userRateId")
    suspend fun deleteUserRate(userRateId: Long)

}