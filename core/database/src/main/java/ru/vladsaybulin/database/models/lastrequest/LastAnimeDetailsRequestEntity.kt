package ru.vladsaybulin.database.models.lastrequest

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import ru.vladsaybulin.database.models.anime.AnimeDetailsEntity

@Entity(
    tableName = "last_anime_request",
    foreignKeys = [
        ForeignKey(
            entity = AnimeDetailsEntity::class,
            parentColumns = ["id"],
            childColumns = ["anime_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LastAnimeDetailsRequestEntity(

    @PrimaryKey
    @ColumnInfo("anime_id")
    val animeId: Long,

    @ColumnInfo("request_date")
    val requestDate: Instant
)