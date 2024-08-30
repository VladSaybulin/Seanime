package ru.vladsaybulin.database.models.anime

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.model.related.RelationType

@Entity(
    tableName = "anime_related",
    foreignKeys = [
        ForeignKey(
            entity = AnimeEntity::class,
            parentColumns = ["id"],
            childColumns = ["anime_id"],
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
class AnimeRelatedEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    @ColumnInfo("anime_id")
    val animeId: Long,

    @ColumnInfo("related_anime_id")
    val relatedAnimeId: Long?,

    @ColumnInfo("related_manga_id")
    val relatedMangaId: Long?,

    @ColumnInfo("relation_type")
    val relationType: RelationType,

    @ColumnInfo("order")
    val order: Int
)