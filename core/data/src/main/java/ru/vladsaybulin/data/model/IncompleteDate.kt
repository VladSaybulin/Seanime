package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.common.IncompleteDateEntity
import ru.vladsaybulin.model.IncompleteDate
import ru.vladsaybulin.network.models.IncompleteDateDto

fun IncompleteDateDto.asEntity() = IncompleteDateEntity(day, month, year)

fun IncompleteDateDto.asIncompleteDate() = IncompleteDate(day, month, year)

fun IncompleteDate.asEntity() = IncompleteDateEntity(day, month, year)