package ru.vladsaybulin.database.models.manga

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.vladsaybulin.database.models.common.asExternalModel
import ru.vladsaybulin.database.models.genre.GenreEntity
import ru.vladsaybulin.database.models.genre.asExternalModel
import ru.vladsaybulin.database.models.text.asExternalModel
import ru.vladsaybulin.model.common.DataSlice
import ru.vladsaybulin.model.manga.MangaDetails

data class PopulatedMangaDetails(
    @Embedded val mangaDetailsEntity: MangaDetailsEntity,

    @Relation(
        entity = MangaEntity::class,
        parentColumn = "id",
        entityColumn = "id"
    )
    val mangaEntity: MangaEntity,

    @Relation(
        entity = GenreEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = MangaGenreCrossRef::class,
            parentColumn = "manga_id",
            entityColumn = "genre_id"
        )
    )
    val genres: List<GenreEntity>,

    @Relation(
        entity = PublisherEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = MangaPublisherCrossRef::class,
            parentColumn = "manga_id",
            entityColumn = "publisher_id"
        )
    )
    val publishers: List<PublisherEntity>
)

fun PopulatedMangaDetails.asExternalModel(): MangaDetails = MangaDetails(
    id = mangaEntity.id,
    originalName = mangaEntity.originalName,
    russianName = mangaEntity.russianName,
    englishName = mangaDetailsEntity.nameEn,
    japaneseName = mangaDetailsEntity.nameJp,
    alternativeName = mangaDetailsEntity.altNames,
    licenseNameRu = mangaDetailsEntity.licenseNameRu,
    poster = mangaEntity.poster?.asExternalModel(),
    kind = mangaEntity.kind,
    score = mangaEntity.score,
    status = mangaEntity.status,
    chapters = mangaEntity.chapters,
    volumes = mangaEntity.volumes,
    airedOn = mangaEntity.airedOn?.asExternalModel(),
    releasedOn = mangaEntity.releasedOn?.asExternalModel(),
    season = mangaDetailsEntity.season?.asExternalModel(),
    description = mangaDetailsEntity.description?.asExternalModel(),
    descriptionSource = mangaDetailsEntity.descriptionSource,
    genres = genres.map { it.asExternalModel() },
    scoreStats = mangaDetailsEntity.scoreStats,
    userRateStatusStats = mangaDetailsEntity.statusStats,
    publishers = publishers.map { it.asExternalModel() }
)