package ru.vladsaybulin.database.models.manga

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "manga_publisher",
    primaryKeys = ["manga_id", "publisher_id"],
    foreignKeys = [
        ForeignKey(
            entity = MangaDetailsEntity::class,
            parentColumns = ["id"],
            childColumns = ["manga_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PublisherEntity::class,
            parentColumns = ["id"],
            childColumns = ["publisher_id"]
        )
    ]
)
data class MangaPublisherCrossRef(

    @ColumnInfo("manga_id")
    val mangaId: Long,

    @ColumnInfo("publisher_id")
    val publisherId: Long
)