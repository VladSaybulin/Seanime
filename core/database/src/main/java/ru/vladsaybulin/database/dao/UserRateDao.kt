package ru.vladsaybulin.database.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.vladsaybulin.database.models.UserRateDbo

interface UserRateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceUserRates(userRates: List<UserRateDbo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceUserRate(userRate: UserRateDbo)

    @Query("DELETE FROM user_rates WHERE id = :userRateId")
    suspend fun deleteUserRate(userRateId: Long)

}