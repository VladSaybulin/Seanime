package ru.vladsaybulin.database.models.anime

import androidx.room.Embedded
import androidx.room.Relation

class PopulatedSimilarAnime(
    @Embedded
    private val entity: AnimeSimilarAnimeCrossRef,

    @Relation(
        entity = AnimeEntity::class,
        parentColumn = "similar_id",
        entityColumn = "id"
    )
    val similarAnimeEntity: AnimeEntity,
)

fun PopulatedSimilarAnime.asExternalModel() = similarAnimeEntity.asExternalModel()