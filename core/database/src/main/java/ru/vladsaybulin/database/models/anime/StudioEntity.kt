package ru.vladsaybulin.database.models.anime

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.vladsaybulin.model.Studio

@Entity(tableName = "studios")
data class StudioEntity(

    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("image_url")
    val imageUrl: String?
)

fun StudioEntity.asExternalModel() = Studio(
    id = id,
    name = name,
    imageUrl = imageUrl
)