package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.filters.FilterPublisherEntity
import ru.vladsaybulin.database.models.manga.PublisherEntity
import ru.vladsaybulin.model.manga.Publisher
import ru.vladsaybulin.network.models.manga.NetworkPublisher

fun NetworkPublisher.asFilterEntity() = FilterPublisherEntity(id, name)

fun NetworkPublisher.asEntity() = PublisherEntity(id, name)

fun NetworkPublisher.asExternalModel() = Publisher(id, name)