package ru.vladsaybulin.database.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.vladsaybulin.database.models.UserRateDbo

interface UserRateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceUserRates(userRates: List<UserRateDbo>)
}