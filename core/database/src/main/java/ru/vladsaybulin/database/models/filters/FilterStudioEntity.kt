package ru.vladsaybulin.database.models.filters

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.vladsaybulin.model.anime.Studio

@Entity(tableName = "filter_studios")
data class FilterStudioEntity(

    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("image_url")
    val imageUrl: String?
)

fun FilterStudioEntity.asExternalModel() = Studio(
    id = id,
    name = name,
    imageUrl = imageUrl
)