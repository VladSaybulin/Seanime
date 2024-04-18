package ru.vladsaybulin.database.models.manga

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.vladsaybulin.model.manga.Publisher

@Entity(tableName = "publishers")
data class PublisherEntity(

    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("name")
    val name: String
)

fun PublisherEntity.asExternalModel() = Publisher(
    id = id,
    name = name
)