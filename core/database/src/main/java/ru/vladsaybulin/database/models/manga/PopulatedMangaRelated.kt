package ru.vladsaybulin.database.models.manga

import androidx.room.Embedded
import androidx.room.Relation
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.anime.asExternalModel
import ru.vladsaybulin.model.related.RelatedAnime
import ru.vladsaybulin.model.related.RelatedManga
import ru.vladsaybulin.model.related.RelatedTitle

data class PopulatedMangaRelated(
    @Embedded
    val mangaRelatedEntity: MangaRelatedEntity,

    @Relation(
        entity = AnimeEntity::class,
        parentColumn = "related_anime_id",
        entityColumn = "id"
    )
    val animeEntity: AnimeEntity?,

    @Relation(
        entity = MangaEntity::class,
        parentColumn = "related_manga_id",
        entityColumn = "id"
    )
    val mangaEntity: MangaEntity?
)

fun PopulatedMangaRelated.asExternalModel() = when {
    animeEntity != null -> RelatedAnime(animeEntity.asExternalModel(), mangaRelatedEntity.relationType)
    mangaEntity != null -> RelatedManga(mangaEntity.asExternalModel(), mangaRelatedEntity.relationType)
    else -> error("animeEntity and mangaEntity can't be null at the same time")

}