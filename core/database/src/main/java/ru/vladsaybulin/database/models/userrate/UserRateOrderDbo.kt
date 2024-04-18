package ru.vladsaybulin.database.models.userrate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "paged_user_rates")
class UserRateOrderDbo(
    @ColumnInfo("user_rate_id")
    @PrimaryKey
    val userId: Long,
    @ColumnInfo("order") val order: Int
)