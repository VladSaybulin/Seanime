package ru.vladsaybulin.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant


@Entity(
    tableName = "calendar_items",
    foreignKeys = [
        ForeignKey(
            entity = AnimeDbo::class,
            parentColumns = ["id"],
            childColumns = ["anime_id"]
        )
    ]
)
data class CalendarItemDbo(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo("episode") val nextEpisode: Int,
    @ColumnInfo("next_episode_at") val nextEpisodeAt: Instant,
    @ColumnInfo("anime_id") val animeId: Long
)