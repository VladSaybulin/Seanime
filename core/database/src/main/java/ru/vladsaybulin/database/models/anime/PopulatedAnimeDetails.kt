package ru.vladsaybulin.database.models.anime

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.vladsaybulin.database.models.common.asExternalModel
import ru.vladsaybulin.database.models.genre.GenreEntity
import ru.vladsaybulin.database.models.genre.asExternalModel
import ru.vladsaybulin.database.models.text.asExternalModel
import ru.vladsaybulin.model.anime.AnimeDetails

data class PopulatedAnimeDetails(

    @Embedded val animeDetailsEntity: AnimeDetailsEntity,

    @Relation(
        entity = AnimeEntity::class,
        parentColumn = "id",
        entityColumn = "id"
    )
    val animeEntity: AnimeEntity,

    @Relation(
        entity = GenreEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = AnimeGenreCrossRef::class,
            parentColumn = "anime_id",
            entityColumn = "genre_id"
        )
    )
    val genres: List<GenreEntity>,

    @Relation(
        entity = StudioEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = AnimeStudioCrossRef::class,
            parentColumn = "anime_id",
            entityColumn = "studio_id"
        )
    )
    val studios: List<StudioEntity>
)

fun PopulatedAnimeDetails.asExternalModel(): AnimeDetails = AnimeDetails(
    id = animeEntity.id,
    originalName = animeEntity.originalName,
    russianName = animeEntity.russianName,
    englishName = animeDetailsEntity.nameEn,
    japaneseName = animeDetailsEntity.nameJp,
    alternativeName = animeDetailsEntity.altNames,
    licenseNameRu = animeDetailsEntity.licenseNameRu,
    poster = animeEntity.poster?.asExternalModel(),
    kind = animeEntity.kind,
    score = animeEntity.score,
    status = animeEntity.status,
    rating = animeDetailsEntity.rating,
    episodes = animeEntity.episodes,
    episodesAired = animeEntity.episodesAired,
    duration = animeDetailsEntity.duration,
    nextEpisodeAt = animeDetailsEntity.nextEpisodeAt,
    airedOn = animeEntity.airedOn?.asExternalModel(),
    releasedOn = animeEntity.releasedOn?.asExternalModel(),
    description = animeDetailsEntity.description?.asExternalModel(),
    descriptionSource = animeDetailsEntity.descriptionSource,
    genres = genres.map { it.asExternalModel() },
    subbers = animeDetailsEntity.subbers,
    dubbers = animeDetailsEntity.dubbers,
    scoreStats = animeDetailsEntity.scoreStats,
    userRateStatusStats = animeDetailsEntity.statusStats,
    studios = studios.map { it.asExternalModel() },
    season = animeDetailsEntity.season?.asExternalModel()
)