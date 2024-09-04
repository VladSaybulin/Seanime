package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.manga.MangaDetailsEntity
import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.database.models.manga.MangaGenreCrossRef
import ru.vladsaybulin.database.models.manga.MangaPublisherCrossRef
import ru.vladsaybulin.database.models.manga.MangaRelatedEntity
import ru.vladsaybulin.network.models.manga.NetworkMangaDetails

fun NetworkMangaDetails.asMangaDetailsEntity() =
    MangaDetailsEntity(
        id = id,
        nameEn = nameEn,
        nameJp = nameJp,
        altNames = alternativeName,
        licenseNameRu = licenseNameRu,
        description = descriptionHtml?.asSeanimeText()?.asSeanimeTextPOJO(),
        descriptionSource = descriptionSource,
        scoreStats = scoreStats?.map { it.asExternalModel() },
        statusStats = userRateStatusStats?.map { it.asExternalModel() }
    )

fun NetworkMangaDetails.asMangaEntity() = MangaEntity(
    id = id,
    originalName = name,
    russianName = nameRu,
    poster = poster?.asPOJO(),
    kind = kind,
    status = status,
    score = score ?: 0f,
    chapters = chapters,
    volumes = volumes,
    airedOn = airedOn?.asPOJO(),
    releasedOn = releasedOn?.asPOJO()
)

fun NetworkMangaDetails.genreEntityShells() =
    genres?.map { it.asEntity() }

fun NetworkMangaDetails.mangaGenreCrossReferences() =
    genres?.map { MangaGenreCrossRef(mangaId = id, genreId = it.id) }

fun NetworkMangaDetails.relatedAnimeEntityShells() =
    related?.mapNotNull { it.anime?.asEntity() }

fun NetworkMangaDetails.relatedMangaEntityShells() =
    related?.mapNotNull { it.manga?.asEntity() }

fun NetworkMangaDetails.mangaRelatedEntities() =
    related?.mapIndexed { index, it ->
        MangaRelatedEntity(
            mangaId = id,
            relatedMangaId = it.manga?.id,
            relatedAnimeId = it.anime?.id,
            relationType = it.relationType,
            order = index
        )
    }

fun NetworkMangaDetails.publisherEntityShells() =
    publishers.map { it.asEntity() }

fun NetworkMangaDetails.mangaPublisherCrossRefs() =
    publishers.map { MangaPublisherCrossRef(id, it.id) }
