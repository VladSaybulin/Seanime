package ru.vladsaybulin.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.vladsaybulin.model.UserRateStatus

@Entity(
    tableName = "user_rates",
    foreignKeys = [
        ForeignKey(
            entity = AnimeDbo::class,
            parentColumns = ["id"],
            childColumns = ["anime_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class UserRateDbo(
    @ColumnInfo("id")
    @PrimaryKey
    val id: Long,
    @ColumnInfo("anime_id") val animeId: Long?,
    @ColumnInfo("manga_id") val mangaId: Long?,
    @ColumnInfo("status") val status: UserRateStatus,
    @ColumnInfo("score") val score: Int,
    @ColumnInfo("episodes") val episodes: Int,
    @ColumnInfo("chapters") val chapters: Int,
    @ColumnInfo("volumes") val volumes: Int,
    @ColumnInfo("rewatches") val rewatches: Int,
    @ColumnInfo("text") val text: String
)