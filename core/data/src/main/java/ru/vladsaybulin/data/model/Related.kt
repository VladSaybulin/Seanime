package ru.vladsaybulin.data.model

import ru.vladsaybulin.model.related.RelatedEntry
import ru.vladsaybulin.network.models.related.NetworkRelated

fun NetworkRelated.asExternalModel() = RelatedEntry(
    anime = anime?.asExternalModel(),
    manga = manga?.asExternalModel(),
    relationType = relationType
)