package ru.vladsaybulin.database.models.lastrequest

import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.datetime.Instant

@Entity(
    tableName = "last_requests",
    primaryKeys = ["type", "target_id"]
)
class LastRequestEntity(

    @ColumnInfo("type")
    val requestType: LastRequestType,

    @ColumnInfo("target_id")
    val targetId: Long,

    @ColumnInfo("date")
    val requestDate: Instant
)