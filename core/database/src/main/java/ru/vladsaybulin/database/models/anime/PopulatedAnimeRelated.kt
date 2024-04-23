package ru.vladsaybulin.database.models.anime

import androidx.room.Embedded
import androidx.room.Relation
import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.database.models.manga.asExternalModel
import ru.vladsaybulin.model.related.RelatedEntry

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

fun PopulatedAnimeRelated.asExternalModel() = RelatedEntry(
    anime = animeEntity?.asExternalModel(),
    manga = mangaEntity?.asExternalModel(),
    relationType = animeRelatedEntity.relationType
)