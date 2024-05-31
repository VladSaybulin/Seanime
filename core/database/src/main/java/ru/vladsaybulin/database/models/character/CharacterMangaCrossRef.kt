package ru.vladsaybulin.database.models.character

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ru.vladsaybulin.database.models.manga.MangaEntity

@Entity(
    tableName = "character_manga",
    primaryKeys = ["character_id", "manga_id"],
    foreignKeys = [
        ForeignKey(
            entity = CharacterDetailsEntity::class,
            parentColumns = ["id"],
            childColumns = ["character_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MangaEntity::class,
            parentColumns = ["id"],
            childColumns = ["manga_id"]
        ),
    ]
)
data class CharacterMangaCrossRef(

    @ColumnInfo("character_id")
    val characterId: Long,

    @ColumnInfo("manga_id")
    val mangaId: Long

)