package ru.vladsaybulin.database.models.userrate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "in_progress_user_rates",
    foreignKeys = [
        ForeignKey(
            entity = UserRateEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_rate_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class InProgressUserRateEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_rate_id")
    val userRateId: Long,
)