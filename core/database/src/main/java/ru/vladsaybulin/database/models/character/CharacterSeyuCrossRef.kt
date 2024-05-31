package ru.vladsaybulin.database.models.character

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ru.vladsaybulin.database.models.person.PersonEntity

@Entity(
    tableName = "character_seyu",
    primaryKeys = ["character_id", "seyu_id"],
    foreignKeys = [
        ForeignKey(
            entity = CharacterDetailsEntity::class,
            parentColumns = ["id"],
            childColumns = ["character_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["seyu_id"]
        )
    ]
)
data class CharacterSeyuCrossRef(

    @ColumnInfo("character_id")
    val characterId: Long,

    @ColumnInfo("seyu_id")
    val seyuId: Long

)