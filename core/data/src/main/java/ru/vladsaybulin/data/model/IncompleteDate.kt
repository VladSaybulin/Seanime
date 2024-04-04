package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.IncompleteDateDbo
import ru.vladsaybulin.model.IncompleteDate
import ru.vladsaybulin.network.models.IncompleteDateDto

fun IncompleteDateDto.asDbo() = IncompleteDateDbo(day, month, year)

fun IncompleteDateDto.asIncompleteDate() = IncompleteDate(day, month, year)

fun IncompleteDate.asDbo() = IncompleteDateDbo(day, month, year)