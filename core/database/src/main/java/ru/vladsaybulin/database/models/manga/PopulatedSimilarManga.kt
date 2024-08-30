package ru.vladsaybulin.database.models.manga

import androidx.room.Embedded
import androidx.room.Relation

class PopulatedSimilarManga(
    @Embedded
    private val crossRef: MangaSimilarMangaCrossRef,

    @Relation(
        entity = MangaEntity::class,
        parentColumn = "similar_id",
        entityColumn = "id"
    )
    val similarManga: MangaEntity
)

fun PopulatedSimilarManga.asExternalModel() = similarManga.asExternalModel()