package ru.vladsaybulin.data.model

import ru.vladsaybulin.core.textprocessor.html.HtmlToAnnotatedTextTransformer
import ru.vladsaybulin.database.models.anime.AnimeDetailsEntity
import ru.vladsaybulin.network.models.anime.NetworkAnimeDetails
import javax.inject.Inject

class AnimeDetailsEntityMapper @Inject constructor(
    private val descriptionTransformer: HtmlToAnnotatedTextTransformer
) {
    operator fun invoke(animeDetails: NetworkAnimeDetails): AnimeDetailsEntity =
        with(animeDetails) {
            AnimeDetailsEntity(
                id = id,
                nameEn = nameEn,
                nameJp = nameJp,
                altNames = alternativeName,
                licenseNameRu = licenseNameRu,
                rating = rating,
                duration = duration ?: 0,
                nextEpisodeAt = nextEpisodeAt,
                description = descriptionHtml?.toAnnotatedTextPOJO(descriptionTransformer),
                descriptionSource = descriptionSource,
                subbers = subbers,
                dubbers = dubbers,
                scoreStats = scoreStats?.map { it.asExternalModel() },
                statusStats = userRateStatusStats?.map { it.asExternalModel() },
            )
        }
}