package ru.vladsaybulin.database.models.calendar

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import ru.vladsaybulin.database.models.anime.AnimeEntity


@Entity(
    tableName = "calendar_items",
    foreignKeys = [
        ForeignKey(
            entity = AnimeEntity::class,
            parentColumns = ["id"],
            childColumns = ["anime_id"]
        )
    ],
    indices = [Index("anime_id")]
)
data class CalendarItemDbo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("episode") val nextEpisode: Int,
    @ColumnInfo("next_episode_at") val nextEpisodeAt: Instant,
    @ColumnInfo("duration") val duration: Int?,
    @ColumnInfo("anime_id") val animeId: Long
)