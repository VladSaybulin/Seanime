package ru.vladsaybulin.database.models.character

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ru.vladsaybulin.database.models.anime.AnimeEntity

@Entity(
    tableName = "character_anime",
    primaryKeys = ["character_id", "anime_id"],
    foreignKeys = [
        ForeignKey(
            entity = CharacterDetailsEntity::class,
            parentColumns = ["id"],
            childColumns = ["character_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AnimeEntity::class,
            parentColumns = ["id"],
            childColumns = ["anime_id"]
        ),
    ]
)
data class CharacterAnimeCrossRef(

    @ColumnInfo("character_id")
    val characterId: Long,

    @ColumnInfo("anime_id")
    val animeId: Long

)