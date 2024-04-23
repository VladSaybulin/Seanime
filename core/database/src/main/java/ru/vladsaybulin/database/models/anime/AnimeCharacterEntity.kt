package ru.vladsaybulin.database.models.anime

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ru.vladsaybulin.database.models.character.CharacterEntity

@Entity(
    tableName = "anime_characters",
    primaryKeys = ["anime_id", "character_id"],
    foreignKeys = [
        ForeignKey(
            entity = AnimeDetailsEntity::class,
            parentColumns = ["id"],
            childColumns = ["anime_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CharacterEntity::class,
            parentColumns = ["id"],
            childColumns = ["character_id"]
        )
    ]
)
data class AnimeCharacterEntity(

    @ColumnInfo("anime_id")
    val animeId: Long,

    @ColumnInfo("character_id")
    val characterId: Long,

    @ColumnInfo("is_main")
    val isMain: Boolean
)