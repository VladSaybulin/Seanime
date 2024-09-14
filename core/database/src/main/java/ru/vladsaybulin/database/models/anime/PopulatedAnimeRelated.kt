package ru.vladsaybulin.database.models.anime

import androidx.room.Embedded
import androidx.room.Relation
import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.database.models.manga.asExternalModel
import ru.vladsaybulin.model.related.RelatedAnime
import ru.vladsaybulin.model.related.RelatedManga
import ru.vladsaybulin.model.related.RelatedTitle

data class PopulatedAnimeRelated(

    @Embedded
    val animeRelatedEntity: AnimeRelatedEntity,

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

fun PopulatedAnimeRelated.asExternalModel() = when {
    animeEntity != null -> RelatedAnime(animeEntity.asExternalModel(), animeRelatedEntity.relationType)
    mangaEntity != null -> RelatedManga(mangaEntity.asExternalModel(), animeRelatedEntity.relationType)
    else -> error("animeEntity and mangaEntity can't be null at the same time")
}