package ru.vladsaybulin.database.models.manga

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.model.related.RelationType

@Entity(
    tableName = "manga_related",
    foreignKeys = [
        ForeignKey(
            entity = MangaDetailsEntity::class,
            parentColumns = ["id"],
            childColumns = ["manga_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AnimeEntity::class,
            parentColumns = ["id"],
            childColumns = ["related_anime_id"]
        ),
        ForeignKey(
            entity = MangaEntity::class,
            parentColumns = ["id"],
            childColumns = ["related_manga_id"]
        )
    ]
)
class MangaRelatedEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    @ColumnInfo("manga_id")
    val mangaId: Long,

    @ColumnInfo("related_anime_id")
    val relatedAnimeId: Long?,

    @ColumnInfo("related_manga_id")
    val relatedMangaId: Long?,

    @ColumnInfo("relation_type")
    val relationType: RelationType,

    @ColumnInfo("order")
    val order: Int
)