package ru.vladsaybulin.database.models.manga

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.vladsaybulin.database.models.common.ImageEntity
import ru.vladsaybulin.database.models.common.asExternalModel
import ru.vladsaybulin.database.models.common.IncompleteDateEntity
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.manga.MangaKind

@Entity(tableName = "mangas")
class MangaEntity(

    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("name")
    val originalName: String,

    @ColumnInfo("russian_name")
    val russianName: String?,

    @Embedded("image")
    val poster: ImageEntity?,

    @ColumnInfo("kind")
    val kind: MangaKind,

    @ColumnInfo("status")
    val status: EntryStatus,

    @ColumnInfo("score")
    val score: Float,

    @ColumnInfo("chapters")
    val chapters: Int,

    @ColumnInfo("volumes")
    val volumes: Int,

    @Embedded("aired_on_")
    val airedOn: IncompleteDateEntity?,

    @Embedded("released_on")
    val releasedOn: IncompleteDateEntity?
)

fun MangaEntity.asExternalModel() = Manga(
    id = id,
    name = originalName,
    russianName = russianName,
    poster = poster?.asExternalModel(),
    kind = kind,
    status = status,
    score = score,
    chapters = chapters,
    volumes = volumes,
    airedOn = airedOn?.asExternalModel(),
    releasedOn = releasedOn?.asExternalModel()
)