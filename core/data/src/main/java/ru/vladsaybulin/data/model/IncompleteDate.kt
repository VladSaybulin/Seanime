package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.IncompleteDateDbo
import ru.vladsaybulin.network.models.IncompleteDateDto

fun IncompleteDateDto.asDbo() = IncompleteDateDbo(day, month, year)