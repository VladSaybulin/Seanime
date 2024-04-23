package ru.vladsaybulin.database.models.lastrequest

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import ru.vladsaybulin.database.models.manga.MangaDetailsEntity

@Entity(
    tableName = "last_manga_request",
    foreignKeys = [
        ForeignKey(
            entity = MangaDetailsEntity::class,
            parentColumns = ["id"],
            childColumns = ["manga_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LastMangaDetailsRequestEntity(

    @PrimaryKey
    @ColumnInfo("manga_id")
    val mangaId: Long,

    @ColumnInfo("request_date")
    val requestDate: Instant
)