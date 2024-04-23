package ru.vladsaybulin.database.models.filters

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.vladsaybulin.model.manga.Publisher

@Entity(tableName = "filter_publishers")
data class FilterPublisherEntity(

    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("name")
    val name: String
)

fun FilterPublisherEntity.asExternalModel() = Publisher(
    id = id,
    name = name
)