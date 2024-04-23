package ru.vladsaybulin.database.models.person

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.vladsaybulin.database.models.common.ImagePOJO
import ru.vladsaybulin.database.models.common.asExternalModel
import ru.vladsaybulin.model.person.Person

@Entity(tableName = "person")
data class PersonEntity(

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

fun PersonEntity.asExternalModel() = Person(
    id = id,
    originalName = name,
    russianName = nameRu,
    poster = image?.asExternalModel()
)