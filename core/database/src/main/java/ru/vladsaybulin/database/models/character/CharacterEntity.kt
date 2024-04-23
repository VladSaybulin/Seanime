package ru.vladsaybulin.database.models.character

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.vladsaybulin.database.models.common.ImagePOJO
import ru.vladsaybulin.database.models.common.asExternalModel
import ru.vladsaybulin.model.character.Character

@Entity(tableName = "characters")
data class CharacterEntity(

    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("name_ru")
    val nameRu: String?,

    @Embedded("image_")
    val image: ImagePOJO?
)

fun CharacterEntity.asExternalModel() = Character(
    id = id,
    originalName = name,
    russianName = nameRu,
    poster = image?.asExternalModel()
)