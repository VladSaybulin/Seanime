package ru.vladsaybulin.database.models.manga

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ru.vladsaybulin.database.models.genre.GenreEntity

@Entity(
    tableName = "manga_genre",
    primaryKeys = ["manga_id", "genre_id"],
    foreignKeys = [
        ForeignKey(
            entity = MangaDetailsEntity::class,
            parentColumns = ["id"],
            childColumns = ["manga_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GenreEntity::class,
            parentColumns = ["id"],
            childColumns = ["genre_id"]
        )
    ]
)
data class MangaGenreCrossRef(

    @ColumnInfo("manga_id")
    val mangaId: Long,

    @ColumnInfo("genre_id")
    val genreId: Long
)