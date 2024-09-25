package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.common.IncompleteDatePOJO
import ru.vladsaybulin.model.common.IncompleteDate
import ru.vladsaybulin.network.models.common.NetworkIncompleteDate

fun NetworkIncompleteDate.asPOJO() = IncompleteDatePOJO(day, month, year)

fun NetworkIncompleteDate.asExternalModel() = IncompleteDate(day, month, year)

fun IncompleteDate.asPOJO() = IncompleteDatePOJO(day, month, year)