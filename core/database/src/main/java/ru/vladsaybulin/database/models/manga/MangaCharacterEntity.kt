package ru.vladsaybulin.database.models.manga

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ru.vladsaybulin.database.models.character.CharacterEntity

@Entity(
    tableName = "manga_characters",
    primaryKeys = ["manga_id", "character_id"],
    foreignKeys = [
        ForeignKey(
            entity = MangaEntity::class,
            parentColumns = ["id"],
            childColumns = ["manga_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CharacterEntity::class,
            parentColumns = ["id"],
            childColumns = ["character_id"]
        )
    ]
)
data class MangaCharacterEntity(

    @ColumnInfo("manga_id")
    val mangaId: Long,

    @ColumnInfo("character_id")
    val characterId: Long,

    @ColumnInfo("is_main")
    val isMain: Boolean
)