package ru.vladsaybulin.database.models.genre

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.Genre
import ru.vladsaybulin.model.GenreKind

@Entity(tableName = "genres")
data class GenreEntity(

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

fun GenreEntity.asExternalModel() = Genre(
    id = id,
    englishName = name,
    russianName = nameRu,
    entryType = entryType,
    kind = kind
)