package ru.vladsaybulin.database.models.userrate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.vladsaybulin.model.list.UserRateOrder
import ru.vladsaybulin.model.list.UserRateOrderField

@Entity(tableName = "paged_user_rates")
class PagedUserRateEntity(

    @ColumnInfo("user_rate_id")
    @PrimaryKey
    val userRateId: Long,

    @ColumnInfo("order_field")
    val orderField: UserRateOrderField,

    @ColumnInfo("sort_order")
    val order: UserRateOrder,

    @ColumnInfo("page")
    val page: Int,

    @ColumnInfo("index")
    val index: Int
)