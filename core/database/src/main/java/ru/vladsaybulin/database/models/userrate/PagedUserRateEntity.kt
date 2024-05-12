package ru.vladsaybulin.database.models.userrate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "paged_user_rates")
class PagedUserRateEntity(

    @ColumnInfo("user_rate_id")
    @PrimaryKey
    val userRateId: Long,

    @ColumnInfo("page")
    val page: Int,

    @ColumnInfo("index")
    val index: Int
)