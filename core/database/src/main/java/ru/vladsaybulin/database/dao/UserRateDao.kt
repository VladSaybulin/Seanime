package ru.vladsaybulin.database.dao

import androidx.room.Insert
import androidx.room.Query
import ru.vladsaybulin.database.models.UserRateDbo

interface UserRateDao {

    @Insert
    fun insertUserRates(userRates: List<UserRateDbo>)

    @Query("DELETE FROM user_rates")
    fun deleteAllUserRates()
}