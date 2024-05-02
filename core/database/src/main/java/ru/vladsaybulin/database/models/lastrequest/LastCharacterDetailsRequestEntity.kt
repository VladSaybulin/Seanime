package ru.vladsaybulin.database.models.lastrequest

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import ru.vladsaybulin.database.models.character.CharacterEntity

@Entity(
    tableName = "last_character_request",
    foreignKeys = [
        ForeignKey(
            entity = CharacterEntity::class,
            parentColumns = ["id"],
            childColumns = ["character_id"]
        )
    ]
)
data class LastCharacterDetailsRequestEntity(

    @PrimaryKey
    @ColumnInfo("character_id")
    val characterId: Long,

    @ColumnInfo("request_date")
    val lastRequestDate: Instant
)