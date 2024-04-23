package ru.vladsaybulin.database.models.filters

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.genre.Genre
import ru.vladsaybulin.model.genre.GenreKind

@Entity(tableName = "filter_genres")
data class FilterGenreEntity(

    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("name_ru")
    val nameRu: String?,

    @ColumnInfo("entry_type")
    val entryType: EntryType,

    @ColumnInfo("kind")
    val kind: GenreKind
)

fun FilterGenreEntity.asExternalModel() = Genre(
    id = id,
    englishName = name,
    russianName = nameRu,
    entryType = entryType,
    kind = kind
)