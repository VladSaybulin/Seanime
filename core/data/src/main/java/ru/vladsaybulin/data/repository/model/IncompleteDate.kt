package ru.vladsaybulin.data.repository.model

import ru.vladsaybulin.database.models.IncompleteDateDbo
import ru.vladsaybulin.network.retrofit.models.IncompleteDateDto

fun IncompleteDateDto.asDbo() = IncompleteDateDbo(day, month, year)