package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.manga.PublisherEntity
import ru.vladsaybulin.network.models.NetworkPublisher

fun NetworkPublisher.asEntity() = PublisherEntity(id, name)