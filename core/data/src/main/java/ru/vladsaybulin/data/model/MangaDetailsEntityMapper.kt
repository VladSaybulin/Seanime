package ru.vladsaybulin.data.model

import ru.vladsaybulin.core.textprocessor.html.HtmlToAnnotatedTextTransformer
import ru.vladsaybulin.database.models.manga.MangaDetailsEntity
import ru.vladsaybulin.network.models.manga.NetworkMangaDetails
import javax.inject.Inject

class MangaDetailsEntityMapper @Inject constructor(
    private val descriptionTransformer: HtmlToAnnotatedTextTransformer
) {
    operator fun invoke(mangaDetails: NetworkMangaDetails): MangaDetailsEntity = with(mangaDetails) {
        MangaDetailsEntity(
            id = id,
            nameEn = nameEn,
            nameJp = nameJp,
            altNames = alternativeName,
            licenseNameRu = licenseNameRu,
            description = descriptionHtml?.toAnnotatedTextPOJO(descriptionTransformer),
            descriptionSource = descriptionSource,
            scoreStats = scoreStats?.map { it.asExternalModel() },
            statusStats = userRateStatusStats?.map { it.asExternalModel() },
        )
    }
}